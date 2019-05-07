package services
import models.Method.IN

object Tr069Service extends App {

  val xml = <SOAP-ENV:Envelope><SOAP-ENV:Body><cwmp:Inform></cwmp:Inform></SOAP-ENV:Body></SOAP-ENV:Envelope>

  println(IN.abbr)
}
