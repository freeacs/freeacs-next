package integrationtests
import java.time.{Clock, Instant, ZoneId}

import com.google.inject.AbstractModule
import org.scalatest.TestSuite
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Configuration, Mode}

import scala.util.Random

trait AppWithFreshDatabase extends GuiceOneServerPerSuite {
  this: TestSuite =>
  val dbPrefix = "play"
  val discoveryMode: Boolean
  val authMethod: String

  override def fakeApplication(): Application = {
    val random = Random.nextInt(Integer.MAX_VALUE)
    GuiceApplicationBuilder()
      .overrides(new TestModule)
      .loadConfig(
        env =>
          Configuration.load(
            env,
            Map(
              "app.auth.method"               -> authMethod,
              "app.discovery.mode"            -> discoveryMode.toString,
              "slick.dbs.default.profile"     -> "slick.jdbc.H2Profile$",
              "slick.dbs.default.driver"      -> "slick.driver.H2Driver$",
              "slick.dbs.default.db.driver"   -> "org.h2.Driver",
              "slick.dbs.default.db.url"      -> s"jdbc:h2:mem:$dbPrefix-$random;MODE=MYSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=FALSE",
              "slick.dbs.default.db.user"     -> "",
              "slick.dbs.default.db.password" -> ""
            )
        )
      )
      .in(Mode.Test)
      .build()
  }

  private class TestModule extends AbstractModule {
    override def configure() = {
      val instant    = Instant.parse("2018-08-19T16:02:42.00Z")
      val zoneId     = ZoneId.of("UTC")
      val fixedClock = Clock.fixed(instant, zoneId)
      bind(classOf[Clock]).toInstance(fixedClock)
    }
  }
}
