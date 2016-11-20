package com.github.maji_ky.plantuml

import java.io.File

import org.specs2.mutable.Specification

class ClassDiagramGeneratorSpec extends Specification {

  private val testPrivate = 'hoge
  val testVal = "test"
  var testVar = Map(testPrivate -> 5L)
  def testFunc(a: Int) = (1 to a).map(_.toDouble / 3)

  "generate class diagram" should {
    "can generate this project" in {
      val rootPackage = "com.github.maji_ky"
      val classLoader = Thread.currentThread().getContextClassLoader
      val resourceName = rootPackage.replace('.', '/')
      val url = classLoader.getResource(resourceName)
      val root = new File(url.toURI)
      val sb = new StringBuilder
      ClassDiagramGenerator.generate(classLoader, root, rootPackage)(x => sb.append(x))
      sb.toString must be equalTo """@startuml
                                    |class com.github.maji_ky.plantuml.ClassDiagramGeneratorSpec extends org.specs2.mutable.Specification {
                                    |val testVal: String
                                    |var testVar: Map[Symbol,Long]
                                    |def testFunc(a: Int): IndexedSeq[Double]
                                    |}
                                    |@enduml""".stripMargin
    }
  }

}
