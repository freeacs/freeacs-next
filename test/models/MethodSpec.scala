package models
import org.scalatestplus.play._

class MethodSpec extends PlaySpec {
  import Method._

  "A method" should {
    "be possible to abbreviate" in {
      IN.abbr mustBe "IN"
    }
    "be parsed from a valid cwmp message" in new CwmpContext() {
      val xml = mkInform()
      fromNode(xml) mustBe Some(IN)
    }
  }
}
