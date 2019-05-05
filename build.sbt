name := "freeacs-play"

version := "1.0"

lazy val `untitled` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
resolvers += "jitpack" at "https://jitpack.io"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq( evolutions, jdbc , ehcache , ws , specs2 % Test , guice )
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.9"
libraryDependencies += "org.jfree" % "jfreechart" % "1.5.0"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.16"
libraryDependencies += "com.github.jarlah" % "AuthenticScala" % "v1.0.3"
libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.4-P26-B4"
libraryDependencies += "com.h2database" % "h2" % "1.4.199"

enablePlugins(JavaAppPackaging)
