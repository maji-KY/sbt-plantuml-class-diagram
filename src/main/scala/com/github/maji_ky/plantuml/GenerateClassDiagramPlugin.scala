package com.github.maji_ky.plantuml

import sbt.Keys._
import sbt.{AutoPlugin, Compile, Def}

object GenerateClassDiagramPlugin extends AutoPlugin {

  val autoImport = GenerateClassDiagramKeys

  import GenerateClassDiagramTask._
  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    genClassDiagramPackage := "blank",
    genClassDiagramOutputTo := "dist",
    genClassDiagram <<= generateClassDiagram triggeredBy (compile in Compile)
  )

}
