package com.github.maji_ky.plantuml

import java.io.File
import java.nio.file.{Files, Paths}

import com.github.maji_ky.plantuml.GenerateClassDiagramKeys._
import sbt.Keys._
import sbt._

object GenerateClassDiagramTask {
  lazy val generateClassDiagram = Def.task {
    val rootPackage = genClassDiagramPackage.value
    val outputDir = baseDirectory.value / genClassDiagramOutputTo.value
    val outputPath = outputDir / "class.diag"
    val packageRootPath = rootPackage.replace('.', File.separatorChar)
    val classDir = (classDirectory in Compile).value
    val packageRootDir = classDir / packageRootPath
    val loader = (testLoader in Test).value

    Files.createDirectories(Paths.get(outputDir.toURI))
    val bw = Files.newBufferedWriter(Paths.get(outputPath.toURI))
    val setting = GenerateSetting(rootPackage = rootPackage, ignoreImplicit = genClassDiagramIgnoreImplicit.value, ignoreClassNameReg = genClassDiagramNameFilter.value)
    try {
      ClassDiagramGenerator.generate(loader, packageRootDir, setting) { output =>
        bw.append(output)
      }
    } finally {
      bw.close()
    }
    outputPath
  }
}
