# sbt-plantuml-class-diagram
[![Build Status](https://travis-ci.org/maji-KY/sbt-plantuml-class-diagram.svg?branch=master)](https://travis-ci.org/maji-KY/sbt-plantuml-class-diagram)
[ ![Download](https://api.bintray.com/packages/maji-ky/sbt-plugins/sbt-plantuml-class-diagram/images/download.svg) ](https://bintray.com/maji-ky/sbt-plugins/sbt-plantuml-class-diagram/_latestVersion)

The sbt plug-in that outputs class diagram for PlantUML.

## Install

#### project/plugins.sbt
```
resolvers += Resolver.url("bintray-majiky-repo", url("https://dl.bintray.com/maji-ky/sbt-plugins"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.github.maji_ky" % "sbt-plantuml-class-diagram" % "1.1")
```

#### build.sbt
```
genClassDiagramPackage := "Root.Package.For.Generate.Diagram"

genClassDiagramIgnoreImplicit := true

enablePlugins(GenerateClassDiagramPlugin)
```

## Usage

Generate
```
sbt> genClassDiagram
```
