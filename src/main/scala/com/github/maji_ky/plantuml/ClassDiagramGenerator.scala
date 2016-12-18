package com.github.maji_ky.plantuml

import java.io.{File, FilenameFilter}
import scala.reflect.runtime.universe

object ClassDiagramGenerator {

  def generate(loader: ClassLoader, rootDir: File, setting: GenerateSetting)(fanout: String => Unit) = {

    val dollarFilter = new FilenameFilter {
      override def accept(f: File, name: String): Boolean = {
        !name.contains("$") && (f.isDirectory || name.endsWith(".class"))
      }
    }

    val pathLength = rootDir.getAbsolutePath.length + 1
    val classLength = ".class".length

    fanout("@startuml\n")
    listFiles(rootDir).map(toFQCN).foreach { cn =>
      val clazz = loader.loadClass(cn)

      import scala.reflect.runtime.universe
      val runtimeMirror = universe.runtimeMirror(loader)
      val classSymbol = runtimeMirror.classSymbol(clazz)
      val baseClass = classSymbol.baseClasses.drop(1)
        .takeWhile(x => x.name.toString != "Object" && x.name.toString != "Serializable").take(1)
        .map(x => x.fullName).headOption

      val typ = classSymbol.asType.toType
      val valList = typ.declarations.filter(_.isTerm).filter(_.asTerm.isVal).map(_.name.toString.trim).toList
      val varList = typ.declarations.filter(_.isTerm).filter(_.asTerm.isVar).map(_.name.toString.trim).toList
      val declarations = typ.declarations
        .filter(_.isMethod)
        .filter(_.isPublic)
        .filterNot(_.isSynthetic)
        .filterNot(_.name.toString == "<init>")
        .filterNot(_.name.toString == "$init$")
        .filterNot(_.name.toString.endsWith("_$eq"))
        .filterNot(_.isJava)
        .map { x =>
          val term = x.name.toString match {
            case a if valList.contains(a) => "val "
            case a if varList.contains(a) => "var "
            case _ => "def "
          }
          declToString(term, x.asMethod)
        }

      fanout(
        s"""class $cn${baseClass.map(x => s" extends $x").mkString} {
            |${declarations.mkString("\n")}
            |}
            |""".stripMargin
      )
    }
    fanout("@enduml")

    implicit class RichList[T](self: List[T]) {
      def mkStringIfNonEmpty(start: String, seq: String, end: String): String =
        if (self.nonEmpty) self.mkString(start, seq, end) else ""
    }

    import scala.reflect.runtime.universe.{Symbol => RefSymbol}
    def paramToString(param: RefSymbol): String =
      (if (param.isImplicit) "implicit " else "") +
        param.name +
        ": " +
        param.typeSignature.typeSymbol.name +
        typeToString(param.typeSignature)

    import scala.reflect.runtime.universe.MethodSymbol
    def declToString(term: String, method: MethodSymbol): String =
      term +
        method.name.decodedName +
        method.typeParams.map(_.name).mkStringIfNonEmpty("[", ",", "]") +
        method.paramss.filterNot(setting.ignoreImplicit && _.headOption.exists(_.isImplicit)).map(_.map(paramToString).mkString(", ")).mkStringIfNonEmpty("(", ")(", ")") +
        ": " +
        method.returnType.typeSymbol.name +
        typeToString(method.returnType)

    def typeToString(typ: universe.Type, from: Option[universe.Type] = None): String = {
      val typeSymbol = typ.typeSymbol
      val seenFromType = from.getOrElse(typ)
      if (typeSymbol.isClass) typeSymbol.asClass.typeParams.map { x =>
        val paramType = x.asType.toType.asSeenFrom(seenFromType, typeSymbol.asClass)
        paramType.typeSymbol.name + typeToString(paramType)
      }.mkStringIfNonEmpty("[", ",", "]")
      else
        ""
    }

    def listFiles(dir: File): Seq[File] = {
      Option(dir.listFiles(dollarFilter)).map(_.toSeq.flatMap {
        case f if f.isDirectory => listFiles(f)
        case f => Seq(f)
      }).getOrElse(Seq.empty)
    }

    def toFQCN(f: File) = {
      val fragment = f.getPath.drop(pathLength).dropRight(classLength).replace(File.separatorChar, '.')
      s"${setting.rootPackage}.$fragment"
    }

  }

}
