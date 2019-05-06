object Tables extends {
  // or just use object demo.Tables, which is hard-wired to the driver stated during generation
  val profile = slick.driver.H2Driver
} with demo.Tables


import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

import Tables._
import Tables.profile.api._
import scala.concurrent.ExecutionContext.Implicits.global


object SlickExample extends App {
  // connection info for a pre-populated throw-away, in-memory db for this demo, which is freshly initialized on every run
  val url = "jdbc:h2:mem:test;MODE=MYSQL;INIT=runscript from 'conf/evolutions/default/1.sql'"
  val db = Database.forURL(url, driver = "org.h2.Driver")

  // Using generated code. Our Build.sbt makes sure they are generated before compilation.
  val q = Profile.join(UnitType).on(_.unitTypeId === _.unitTypeId)
    .map { case (co, cp) => (co.profileName, cp.unitTypeName) }

  Await.result(db.run(q.result).map { result =>
    println(result.groupBy { case (co, cp) => co }
      .mapValues(_.map { case (co, cp) => cp })
      .mkString("\n")
    )
  }, 60 seconds)
}
