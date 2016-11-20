# sbt-plantuml-class-diagram
[![Build Status](https://travis-ci.org/maji-KY/sbt-plantuml-class-diagram.svg?branch=master)](https://travis-ci.org/maji-KY/sbt-plantuml-class-diagram)

The sbt plug-in that outputs class diagram for PlantUML.

## Install

#### project/plugins.sbt
```
resolvers += Resolver.url("bintray-majiky-repo", url("https://dl.bintray.com/maji-ky/sbt-plugins"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.github.maji_ky" % "sbt-plantuml-class-diagram" % "1.0")
```

#### build.sbt
```
genClassDiagramPackage := "Root.Package.For.Generate.Diagram"

enablePlugins(GenerateClassDiagramPlugin)
```

## Usage

Generate
```
sbt> genClassDiagram
```
