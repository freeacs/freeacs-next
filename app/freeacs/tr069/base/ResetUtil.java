package freeacs.tr069.base;

import freeacs.dbi.UnitParameter;
import freeacs.dbi.Unittype;
import freeacs.dbi.UnittypeParameter;
import freeacs.dbi.util.SystemParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ResetUtil {
  private static final Logger log = LoggerFactory.getLogger(ResetUtil.class);

  public static void resetReboot(SessionDataI sessionData) {
    log.debug("The reboot parameter is reset to 0 and the reboot will be executed");
    freeacs.dbi.Unittype unittype = sessionData.getUnittype();
    UnittypeParameter utp = unittype.getUnittypeParameters().getByName(SystemParameters.RESTART);
    List<UnitParameter> unitParameters = new ArrayList<>();
    UnitParameter up = new UnitParameter(utp, sessionData.getUnitId(), "0", sessionData.getProfile());
    unitParameters.add(up);
    unitParameters.forEach(sessionData.getUnit()::toWriteQueue);
  }

  public static void resetReset(SessionDataI sessionData) {
    log.debug("The reset parameter is reset to 0 and the factory reset will be executed");
    Unittype unittype = sessionData.getUnittype();
    UnittypeParameter utp = unittype.getUnittypeParameters().getByName(SystemParameters.RESET);
    List<UnitParameter> unitParameters = new ArrayList<>();
    UnitParameter up = new UnitParameter(utp, sessionData.getUnitId(), "0", sessionData.getProfile());
    unitParameters.add(up);
    unitParameters.forEach(sessionData.getUnit()::toWriteQueue);
  }
}
