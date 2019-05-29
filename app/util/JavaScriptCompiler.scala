package util
import javax.script.Invocable
import jdk.nashorn.api.scripting.NashornScriptEngineFactory

object JavaScriptCompiler {
  def compile[A](code: String): java.util.Map[String, Any] => A = {
    val engine = new NashornScriptEngineFactory().getScriptEngine("--no-java")
    val wrapper =
      s"""
         |var quit = function(){ throw 'Unsupported operation: quit'; }
         |var exit = function(){ throw 'Unsupported operation: exit'; }
         |var load = function(){ throw 'Unsupported operation: load'; }
         |var print = function(){ throw 'Unsupported operation: print'; }
         |var echo = function(){ throw 'Unsupported operation: echo'; }
         |var readLine = function(){ throw 'Unsupported operation: readLine'; }
         |var readFully = function(){ throw 'Unsupported operation: readFully'; }
         |var loadWithNewGlobal = function(){ throw 'Unsupported operation: loadWithNewGlobal'; }
         |function wrapper(context) {
         |  $code
         |}
      """.stripMargin
    engine.eval(wrapper)
    engine.asInstanceOf[Invocable].invokeFunction("wrapper", _).asInstanceOf[A]
  }
}
