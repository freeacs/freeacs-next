package integrationtests

import models.{SessionData, SystemParameters}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.Utility.trim

class NoAuthenticationAndDiscoveryModeDisabledTest
    extends PlaySpec
    with GuiceOneServerPerSuite
    with AppWithFreshDatabase {

  override val dbPrefix      = "noauthnodiscovery"
  override val authMethod    = "none"
  override val discoveryMode = false

  "can provision a new unit" in new AbstractIntegrationTest(port, app.injector) {
    // 1. IN
    var response = post(Some(informRequest))
    response.status mustBe 200
    trim(response.xml) mustBe trim(informResponse)
    val unit = unsafeGetUnit("000000-FakeProductClass-FakeSerialNumber")
    unit.profile.name mustBe "Default"
    unit.profile.unitType.name mustBe "FakeProductClass"
    unit.profile.params.length mustBe 0
    unit.profile.unitType.params.length mustBe 33
    unit.unitTypeParams.length mustBe 33
    unit.params.length mustBe 3
    unit.params
      .find(_.unitTypeParamName.endsWith(SessionData.deviceSoftwareVersionSuffix))
      .flatMap(_.value) mustBe Some("V5.2.10P4T26")
    unit.params
      .find(_.unitTypeParamName.equals(SystemParameters.LAST_CONNECT_TMS.name))
      .flatMap(_.value) mustBe Some("2018-08-19T16:02:42")
    unit.params
      .find(_.unitTypeParamName.equals(SystemParameters.FIRST_CONNECT_TMS.name))
      .flatMap(_.value) mustBe Some("2018-08-19T16:02:42")

    // 2. EM
    response = post(None)
    response.status mustBe 200
    trim(response.xml) mustBe trim(getParameterValuesRequest)

    // 3. GPVr
    response = post(Some(getParameterValuesResponse))
    response.status mustBe 200
    response.body mustBe ""
    response.header("Connection") mustBe Some("close")
  }
}
