logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.21")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.0.0")
