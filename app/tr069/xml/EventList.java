package tr069.xml;

import java.util.ArrayList;
import java.util.List;

public class EventList {
  public static final String ID = "EventList";

  private List<EventStruct> eventList = new ArrayList<>();

  public EventList() {
  }

  void addEvent(EventStruct event) {
    this.eventList.add(event);
  }

  public EventList clone() {
    EventList clonedEventList = new EventList();
    for (EventStruct event : eventList) {
      EventStruct clonedEvent = new EventStruct(event.getEventCode(), event.getCommandKey());
      clonedEventList.addEvent(clonedEvent);
    }
    return clonedEventList;
  }

  public List<EventStruct> getEventList() {
    return eventList;
  }

  public void setEventList(List<EventStruct> eventList) {
    this.eventList = eventList;
  }
}
