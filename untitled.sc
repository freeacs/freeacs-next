import util.JavaScriptCompiler
import collection.JavaConverters._

val context = Map[String, Any]("callMe" -> new java.util.function.Function[String, String]() {
    override def apply(t: String) =
      "Hello, " + t
  }).asJava

JavaScriptCompiler.compile[String](
  """
    | var callMe = context.get("callMe");
    | return callMe("World");
  """.stripMargin).apply(context)
