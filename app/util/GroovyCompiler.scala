package util

import groovy.lang.{Binding, Closure, GroovyShell, Script}
import org.codehaus.groovy.control.CompilerConfiguration
import org.kohsuke.groovy.sandbox.{GroovyValueFilter, SandboxTransformer}

object GroovyCompiler extends GroovyValueFilter with App {
  val ALLOWED_TYPES: Set[Class[_]] = Set(
    classOf[GroovyContext],
    classOf[java.lang.String],
    classOf[Integer],
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

  val cc = new CompilerConfiguration()
  cc.addCompilationCustomizers(new SandboxTransformer())
  val binding = new Binding()
  binding.setProperty("context", new GroovyContext())
  val sh = new GroovyShell(binding, cc)

  register()

  sh.evaluate(""" context.exit(); """)  // Success because its GroovyContext
  sh.evaluate(""" System.exit(-1); """) // fails because its Class
  sh.evaluate(""" println "Hei"; """)   // Success because its String
  sh.evaluate(""" println 0.23 """)     // fails because its BigDecimal
}
