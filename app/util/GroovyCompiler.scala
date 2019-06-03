package util

import groovy.lang.{Binding, Closure, GroovyShell, Script}
import org.codehaus.groovy.control.CompilerConfiguration
import org.kohsuke.groovy.sandbox.{GroovyValueFilter, SandboxTransformer}

object GroovyCompiler extends GroovyValueFilter with App {
  private class TypeFilter extends GroovyValueFilter {
    val ALLOWED_TYPES: Set[Class[_]] = Set(
      classOf[GroovyContext],
      classOf[java.lang.String],
      classOf[java.lang.Integer],
      classOf[java.lang.Boolean],
      classOf[java.lang.Double],
      classOf[java.math.BigDecimal],
      classOf[java.lang.CharSequence],
      classOf[java.lang.Float],
      classOf[java.lang.Short],
      classOf[java.lang.Byte],
      classOf[java.lang.Long]
    )
    override def filter(obj: Object): Object = {
      if (obj == null || ALLOWED_TYPES.contains(obj.getClass))
        return obj
      if (obj.isInstanceOf[Script] || obj.isInstanceOf[Closure[_]])
        return obj // access to properties of compiled groovy script
      throw new SecurityException("Oops, unexpected type: " + obj.getClass)
    }
  }

  new TypeFilter().register()

  private val cc = new CompilerConfiguration()
  cc.addCompilationCustomizers(new SandboxTransformer())
  private val binding = new Binding()
  binding.setProperty("context", new GroovyContext())
  private val sh = new GroovyShell(binding, cc)

  def execute(script: String): String = sh.evaluate(script).asInstanceOf[String]

  println(execute(""" 
      |def sayHello(name) {
      | "Hello, " + name
      |}
      |
      |sayHello("World");
      |""".stripMargin))
}
