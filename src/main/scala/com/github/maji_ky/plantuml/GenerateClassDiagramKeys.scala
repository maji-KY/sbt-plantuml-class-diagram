package com.github.maji_ky.plantuml

import sbt._

object GenerateClassDiagramKeys {
  val genClassDiagramPackage = settingKey[String]("specify root package to find classes")
  val genClassDiagramOutputTo = settingKey[String]("output directory")
  //  val genClassDiagramExtendsFilter = settingKey[String]("specify superclass class to filter classes")
  val genClassDiagram = taskKey[File]("generate class diagram")
}
