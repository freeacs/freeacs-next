package freeacs.dbi;

public class JobFlag {

  private JobType type;
  private JobServiceWindow serviceWindow;

  public JobFlag(String flagStr) {
    String typeStr = flagStr.split("\\|")[0];
    try {
      type = JobType.valueOf(typeStr);
    } catch (Throwable t) { // Convert from old jobtype
      if ("SCRIPT".equals(typeStr)) {
        type = JobType.TR069_SCRIPT;
      }
    }
    serviceWindow = JobServiceWindow.valueOf(flagStr.split("\\|")[1]);
  }

  public JobFlag(JobType jobType, JobServiceWindow jobServiceWindow) {
    type = jobType;
    serviceWindow = jobServiceWindow;
  }

  public JobType getType() {
    return type;
  }

  public void setType(JobType type) {
    this.type = type;
  }

  public JobServiceWindow getServiceWindow() {
    return serviceWindow;
  }

  public String toString() {
    return type + "|" + serviceWindow;
  }
}
