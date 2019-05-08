package models
import org.scalatestplus.play.PlaySpec

class EventStructSpec extends PlaySpec {

  "A EventStruct" should {
    "be possible to parse from a valid cwmp inform" in new CwmpContext() {
      val xml    = mkInform()
      val events = EventStruct.fromNode(xml)
      events.length mustBe 3
    }
  }
}
