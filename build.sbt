organization := "com.github.maji_ky"

name := "sbt-plantuml-class-diagram"

version := "0.1-SNAPSHOT"

sbtPlugin := true

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen"
)

scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.8.4" % "test"
)

bintrayOrganization in bintray := Some("maji-ky")

bintrayRepository in bintray := "sbt-plantuml-class-diagram"

licenses += ("GPL-3.0", url("http://www.gnu.org/licenses/gpl-3.0.html"))

// sbt scripted
ScriptedPlugin.scriptedSettings

scriptedBufferLog := false

scriptedLaunchOpts <+= version { "-Dplugin.version=" + _ }