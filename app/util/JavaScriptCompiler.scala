package util
import javax.script.ScriptEngineManager
import javax.script.Invocable

object JavaScriptCompiler {
  def compile[A](code: String): Map[String, Any] => A = {
    val factory = new ScriptEngineManager
    val engine  = factory.getEngineByName("JavaScript")
    val wrapper =
      s"""
        |function wrapper(context) {
        |  $code
        |}
      """.stripMargin
    engine.eval(wrapper)
    engine.asInstanceOf[Invocable].invokeFunction("wrapper", _).asInstanceOf[A]
  }
}
