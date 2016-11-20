lazy val root = project.in(file(".")).
  aggregate(p1, p2).
  settings(
    organization := "com.github.maji_ky",
    name := "sbt-plantuml-class-diagram-test",
    version := "0.1-SNAPSHOT",
    genClassDiagramPackage := "example"
  ).enablePlugins(GenerateClassDiagramPlugin)

lazy val p1 = project.in(file("p1")).settings(
  genClassDiagramPackage := "example"
).enablePlugins(GenerateClassDiagramPlugin)
lazy val p2 = project.in(file("p2")).settings(
  genClassDiagramPackage := "example"
).enablePlugins(GenerateClassDiagramPlugin)