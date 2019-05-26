package integrationtests

import models.{SessionData, SystemParameters}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Configuration, Mode}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.Utility.trim

class NoAuthenticationAndDiscoveryModeEnabledTest extends PlaySpec with GuiceOneServerPerTest {

  override def fakeApplication(): Application =
    GuiceApplicationBuilder()
      .loadConfig(
        env =>
          Configuration.load(
            env,
            Map(
              "app.auth.method"    -> "none",
              "app.discovery.mode" -> "true"
            )
        )
      )
      .in(Mode.Test)
      .build()

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
      .flatMap(_.value.map(_.length)) mustBe Some(26)
    unit.params
      .find(_.unitTypeParamName.equals(SystemParameters.FIRST_CONNECT_TMS.name))
      .flatMap(_.value.map(_.length)) mustBe Some(26)

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
