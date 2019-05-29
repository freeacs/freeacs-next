package util

import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox

object ScriptCompiler {

  def compile[A](code: String): Map[String, Any] => A = {
    val tb      = runtimeMirror(getClass.getClassLoader).mkToolBox()
    val tree    = tb.parse(s"""
         |def wrapper(context: Map[String, Any]): Any = {
         |  $code
         |}
         |wrapper _
      """.stripMargin)
    val f       = tb.compile(tree)
    val wrapper = f()
    wrapper.asInstanceOf[Map[String, Any] => A]
  }

  /**
 * val script = compile(""" println(context("a").asInstanceOf[Int].toDouble) """).apply(Map("a" -> 42))
 * println(script)
 */
}
