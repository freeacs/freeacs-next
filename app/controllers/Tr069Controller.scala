package controllers

import java.time.LocalDateTime

import akka.Done
import config.Settings
import io.kanaka.monadic.dsl._
import models.SystemParameters._
import models._
import play.api.Logging
import play.api.cache.AsyncCacheApi
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{ProfileService, UnitService, UnitTypeService}
import util.MonadTransformers._

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Node

class Tr069Controller(
    cc: ControllerComponents,
    unitService: UnitService,
    profileService: ProfileService,
    unitTypeService: UnitTypeService,
    cache: AsyncCacheApi,
    secureAction: SecureAction,
    settings: Settings
)(
    implicit ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport
    with Logging {

  def provision = secureAction.authenticate.async { implicit request =>
    for {
      sessionData       <- getSessionData(request) ?| InternalServerError("Failed to get session")
      (updated, result) <- processRequest(sessionData, payload(request)) ?| (e => InternalServerError(e))
      _                 <- putSessionData(request, sessionData, updated) ?| InternalServerError("Failed to update session")
    } yield {
      if (updated.unit.isDefined)
        result.withSession(request.session)
      else
        result
    }
  }

  private def payload(request: SecureRequest[AnyContent]) =
    request.body.asXml.flatMap(_.headOption).getOrElse(<Empty />)

  private def processRequest(
      sessionData: SessionData,
      payload: Node
  ): Future[Either[String, (SessionData, Result)]] = {
    val method = CwmpMethod.fromNode(payload).getOrElse(CwmpMethod.EM)
    (method match {
      case CwmpMethod.IN =>
        processInform(sessionData, payload)
      case otherMethod =>
        logger.debug(s"Got ${otherMethod.abbr} method, answering with NotImplemented")
        Future.successful(Right((sessionData, NotImplemented)))
    }).map(_.map {
      case (finalSessionData, result) =>
        (finalSessionData.copy(requests = finalSessionData.requests ++ Seq(method)), result)
    })
  }

  private def processInform(
      sessionData: SessionData,
      payload: Node
  ): Future[Either[String, (SessionData, Result)]] =
    getHeader(sessionData, payload)
      .flatMap(getDeviceId(_, payload))
      .flatMap(getEvents(_, payload))
      .flatMap(getParams(_, payload))
      .map(
        loadUnit(_)
          .map(sessionData => sessionData.copy(username = sessionData.unitId))
          .flatMap(maybeCreateUnit)
          .flatMap(maybeUpdateAcsParams)
          .map { sessionData =>
            if (sessionData.unit.isDefined) {
              val debug  = pprint.PPrinter.BlackWhite.tokenize(sessionData, height = 200).mkString
              val unitId = sessionData.unitId.getOrElse("anonymous")
              logger.warn(s"Inform from unit [$unitId]. SessionData:\n$debug")
              logger.warn("Keyroot " + sessionData.keyRoot)
              (sessionData, createInformResponse(sessionData.cwmpVersion))
            } else {
              logger.warn(s"Unit data is missing")
              (sessionData, Ok)
            }
          }
      )
      .mapToFutureEither

  private def loadUnit(sessionData: SessionData): Future[SessionData] =
    sessionData.unitId match {
      case Some(username) =>
        unitService.find(username).map { maybeUnit =>
          sessionData.copy(unit = maybeUnit)
        }
      case _ =>
        Future.successful(sessionData)
    }

  private def maybeCreateUnit(sessionData: SessionData): Future[SessionData] =
    sessionData.unit match {
      case None if settings.discoveryMode =>
        unitService
          .createAndReturnUnit(
            sessionData.unsafeGetUnitId,
            "Default",
            sessionData.unsafeGetProductClass(settings.appendHwVersion)
          )
          .map(unit => sessionData.copy(unit = Some(unit)))
      case _ =>
        logger.debug("Unit is already loaded or discovery mode is not enabled. Not creating.")
        Future.successful(sessionData)
    }

  private def maybeUpdateAcsParams(sessionData: SessionData): Future[SessionData] =
    sessionData.unit match {
      case Some(unit) =>
        for {
          firstConnect <- getTimestamp(unit, FIRST_CONNECT_TMS, update = false)
          lastConnect  <- getTimestamp(unit, LAST_CONNECT_TMS, update = true)
          deviceParams <- getDeviceParameters(sessionData, unit)
          filteredParams = filterParams(Seq(firstConnect, lastConnect) ++ deviceParams, unit.params)
          _           <- unitService.upsertParameters(filteredParams)
          updatedUnit <- unitService.find(unit.unitId)
        } yield sessionData.copy(unit = updatedUnit)
      case _ =>
        Future.successful(sessionData)
    }

  private def filterParams(
      paramsToUpsert: Seq[AcsUnitParameter],
      existingParams: Seq[AcsUnitParameter]
  ) =
    paramsToUpsert.filter(
      p =>
        existingParams.exists(
          up => up.unitTypeParamName == p.unitTypeParamName && up.value != p.value
        ) || p.value.isDefined
    )

  private def getDeviceParameters(sessionData: SessionData, unit: AcsUnit): Future[Seq[AcsUnitParameter]] = {
    if (sessionData.keyRoot.isEmpty) {
      return Future.successful(Seq.empty)
    }
    for {
      softwareVersion <- getOrCreateUnitTypeParameter(unit, NamedParameter(sessionData.SOFTWARE_VERSION, "R"))
      pII             <- getOrCreateUnitTypeParameter(unit, NamedParameter(sessionData.PERIODIC_INFORM_INTERVAL, "RW"))
      connReqUrl      <- getOrCreateUnitTypeParameter(unit, NamedParameter(sessionData.CONNECTION_URL, "R"))
      connReqUser     <- getOrCreateUnitTypeParameter(unit, NamedParameter(sessionData.CONNECTION_USERNAME, "R"))
      connReqPass     <- getOrCreateUnitTypeParameter(unit, NamedParameter(sessionData.CONNECTION_PASSWORD, "R"))
    } yield
      Seq(
        makeUnitParam(unit.unitId, softwareVersion, sessionData.params),
        makeUnitParam(unit.unitId, pII, sessionData.params),
        makeUnitParam(unit.unitId, connReqUrl, sessionData.params),
        makeUnitParam(unit.unitId, connReqUser, sessionData.params),
        makeUnitParam(unit.unitId, connReqPass, sessionData.params)
      )
  }

  private def makeUnitParam(
      unitId: String,
      softwareVersion: AcsUnitTypeParameter,
      params: Seq[ParameterValueStruct]
  ) =
    AcsUnitParameter(
      unitId,
      softwareVersion.unitTypeParamId,
      softwareVersion.name,
      params.find(_.name == softwareVersion.name).map(_.value)
    )

  private def getTimestamp(
      unit: AcsUnit,
      param: Parameter,
      update: Boolean
  ): Future[AcsUnitParameter] = {
    getOrCreateUnitTypeParameter(unit, param).map { unitTypeParameter =>
      val ts = LocalDateTime.now().toString
      unit.params
        .find(_.unitTypeParamName == param.name)
        .map { p =>
          if (update)
            p.copy(value = Some(ts))
          else
            p
        }
        .getOrElse(
          AcsUnitParameter(unit.unitId, unitTypeParameter.unitTypeParamId, unitTypeParameter.name, Some(ts))
        )
    }
  }

  private def getOrCreateUnitTypeParameter(unit: AcsUnit, param: Parameter) =
    unit.unitTypeParams.find(_.name == param.name) match {
      case Some(unitTypeParameter) =>
        Future.successful(unitTypeParameter)
      case None =>
        unitTypeService.createUnitTypeParameter(
          unit.profile.unitType.unitTypeId.get,
          param.name,
          param.flag
        )
    }

  private def createInformResponse(cwmpVersion: String): Result =
    Ok(
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
        <soapenv:Body>
          <cwmp:InformResponse xmlns:cwmp={s"urn:dslforum-org:cwmp-$cwmpVersion"}>
            <MaxEnvelopes>1</MaxEnvelopes>
          </cwmp:InformResponse>
        </soapenv:Body>
      </soapenv:Envelope>
    ).withHeaders("SOAPAction" -> "")

  private def getHeader(sessionData: SessionData, payload: Node): Either[String, SessionData] =
    HeaderStruct.fromNode(payload) match {
      case Some(header) => Right(sessionData.copy(header = Some(header)))
      case None         => Left("Missing header in payload")
    }

  private def getEvents(sessionData: SessionData, payload: Node): Either[String, SessionData] =
    EventStruct.fromNode(payload) match {
      case seq: Seq[EventStruct] if seq.nonEmpty => Right(sessionData.copy(events = seq))
      case _                                     => Left("Missing events")
    }

  private def getParams(sessionData: SessionData, payload: Node): Either[String, SessionData] =
    ParameterValueStruct.fromNode(payload) match {
      case seq: Seq[ParameterValueStruct] if seq.nonEmpty => Right(sessionData.copy(params = seq))
      case _                                              => Left("Missing params")
    }

  private def getDeviceId(sessionData: SessionData, payload: Node): Either[String, SessionData] =
    DeviceIdStruct.fromNode(payload) match {
      case Some(deviceIdStruct) => Right(sessionData.copy(deviceId = Some(deviceIdStruct)))
      case None                 => Left("Missing deviceId")
    }

  private def getSessionData(request: SecureRequest[AnyContent]): Future[SessionData] =
    cache.getOrElseUpdate[SessionData](sessionDataKey(request.sessionId)) {
      Future.successful(SessionData(sessionId = request.sessionId, username = request.username))
    }

  private def putSessionData(
      request: SecureRequest[AnyContent],
      sessionData: SessionData,
      updatedSessionData: SessionData
  ): Future[Done] =
    if (sessionData != updatedSessionData)
      cache.set(sessionDataKey(request.sessionId), updatedSessionData)
    else
      Future.successful(Done)

  private def sessionDataKey(sessionId: String) = s"$sessionId-data"
}
