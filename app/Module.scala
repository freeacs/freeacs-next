import java.time.Clock

import com.google.inject.AbstractModule

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
  }

}
