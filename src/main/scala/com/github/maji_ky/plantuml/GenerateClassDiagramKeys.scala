package com.github.maji_ky.plantuml

import sbt._

import scala.util.matching.Regex

object GenerateClassDiagramKeys {
  val genClassDiagramPackage = settingKey[String]("specify root package to find classes")
  val genClassDiagramOutputTo = settingKey[String]("output directory")
  val genClassDiagramIgnoreImplicit = settingKey[Boolean]("ignore implicit parameter")
  val genClassDiagramNameFilter = settingKey[Option[Regex]]("specify class name Regex to filter classes")
  val genClassDiagram = taskKey[File]("generate class diagram")
}
