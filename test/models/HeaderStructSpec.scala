package models
import org.scalatestplus.play.PlaySpec

class HeaderStructSpec extends PlaySpec {
  "A HeaderStruct" should {
    "be able to parse a header" in new CwmpContext() {
      val xml    = mkIN()
      val header = HeaderStruct.fromNode(xml)
      header mustBe Some(HeaderStruct("1", holdRequests = false, noMoreRequests = false))
    }
  }
}
