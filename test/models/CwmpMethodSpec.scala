package models
import org.scalatestplus.play._

class CwmpMethodSpec extends PlaySpec {
  import CwmpMethod._

  "A method" should {
    "be possible to abbreviate" in {
      IN.abbr mustBe "IN"
    }
    "be parsed from a valid cwmp message" in new CwmpContext() {
      val xml = mkIN()
      fromNode(xml) mustBe Some(IN)
    }
  }
}
