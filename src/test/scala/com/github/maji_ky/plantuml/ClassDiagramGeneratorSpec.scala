package com.github.maji_ky.plantuml

import java.io.File

import org.specs2.mutable.Specification

import scala.util.{Failure, Try}

class ClassDiagramGeneratorSpec extends Specification {

  sequential

  private val testPrivate = 'hoge
  val testVal = "test"
  var testVar = Map(testPrivate -> 5L)
  def testFunc(a: Int) = (1 to a).map(_.toDouble / 3)

  "generate class diagram" should {
    val rootPackage = "com.github.maji_ky"
    val classLoader = Thread.currentThread().getContextClassLoader
    val resourceName = rootPackage.replace('.', '/')
    val url = classLoader.getResource(resourceName)
    val root = new File(url.toURI)

    "can generate this project" in {
      val sb = new StringBuilder
      val setting = GenerateSetting(rootPackage = rootPackage, ignoreImplicit = false, ignoreClassNameReg = None)
      ClassDiagramGenerator.generate(classLoader, root, setting)(x => sb.append(x))
      sb.toString must be equalTo """@startuml
                                    |class com.github.maji_ky.plantuml.CaseClass {
                                    |val foo: String
                                    |}
                                    |class com.github.maji_ky.plantuml.ClassDiagramGeneratorSpec extends org.specs2.mutable.Specification {
                                    |val testVal: String
                                    |var testVar: Map[Symbol,Long]
                                    |def testFunc(a: Int): IndexedSeq[Double]
                                    |}
                                    |class com.github.maji_ky.plantuml.ClassTest {
                                    |val a: A
                                    |def someType[B](b: B)(implicit i: B): A
                                    |def nested(arg: Try[Seq[Option[Int]]]): Try[Seq[Option[A]]]
                                    |}
                                    |class com.github.maji_ky.plantuml.ExtendsTest extends scala.math.Ordering {
                                    |def compare(x: Seq[Option[A]], y: Seq[Option[A]]): Int
                                    |}
                                    |class com.github.maji_ky.plantuml.TraitTest {
                                    |val traitValue: ClassTest[Int]
                                    |}
                                    |@enduml""".stripMargin
    }

    "ignore implicit parameter" in {
      val sb = new StringBuilder
      val setting = GenerateSetting(rootPackage = rootPackage, ignoreImplicit = true, ignoreClassNameReg = None)
      ClassDiagramGenerator.generate(classLoader, root, setting)(x => sb.append(x))
      sb.toString must be equalTo """@startuml
                                    |class com.github.maji_ky.plantuml.CaseClass {
                                    |val foo: String
                                    |}
                                    |class com.github.maji_ky.plantuml.ClassDiagramGeneratorSpec extends org.specs2.mutable.Specification {
                                    |val testVal: String
                                    |var testVar: Map[Symbol,Long]
                                    |def testFunc(a: Int): IndexedSeq[Double]
                                    |}
                                    |class com.github.maji_ky.plantuml.ClassTest {
                                    |val a: A
                                    |def someType[B](b: B): A
                                    |def nested(arg: Try[Seq[Option[Int]]]): Try[Seq[Option[A]]]
                                    |}
                                    |class com.github.maji_ky.plantuml.ExtendsTest extends scala.math.Ordering {
                                    |def compare(x: Seq[Option[A]], y: Seq[Option[A]]): Int
                                    |}
                                    |class com.github.maji_ky.plantuml.TraitTest {
                                    |val traitValue: ClassTest[Int]
                                    |}
                                    |@enduml""".stripMargin
    }

    "ignore class name match" in {
      val sb = new StringBuilder
      val setting = GenerateSetting(rootPackage = rootPackage, ignoreImplicit = true, ignoreClassNameReg = Some(".*Test$".r))
      ClassDiagramGenerator.generate(classLoader, root, setting)(x => sb.append(x))
      sb.toString must be equalTo """@startuml
                                    |class com.github.maji_ky.plantuml.CaseClass {
                                    |val foo: String
                                    |}
                                    |class com.github.maji_ky.plantuml.ClassDiagramGeneratorSpec extends org.specs2.mutable.Specification {
                                    |val testVal: String
                                    |var testVar: Map[Symbol,Long]
                                    |def testFunc(a: Int): IndexedSeq[Double]
                                    |}
                                    |@enduml""".stripMargin
    }
  }
}

class ClassTest[A](val a: A) {
  def someType[B](b: B)(implicit i: B) = a
  def nested(arg:Try[Seq[Option[Int]]]): Try[Seq[Option[A]]] = Failure(new Exception)
}

class ExtendsTest[A] extends Ordering[Seq[Option[A]]] {
  override def compare(x: Seq[Option[A]], y: Seq[Option[A]]): Int = 0
}

trait TraitTest {
  type Alias = ClassTest[Int]
  val traitValue: Alias = new Alias(0)
}

case class CaseClass(foo: String)