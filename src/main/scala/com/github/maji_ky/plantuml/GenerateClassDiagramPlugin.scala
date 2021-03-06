package com.github.maji_ky.plantuml

import sbt.{AutoPlugin, Def}

object GenerateClassDiagramPlugin extends AutoPlugin {

  val autoImport = GenerateClassDiagramKeys

  import GenerateClassDiagramTask._
  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    genClassDiagramPackage := "blank",
    genClassDiagramOutputTo := "dist",
    genClassDiagramIgnoreImplicit := true,
    genClassDiagramNameFilter := None,
    genClassDiagram := generateClassDiagram.value
  )

}
