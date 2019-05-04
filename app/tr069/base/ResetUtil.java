package tr069.base;

import dbi.UnitParameter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ResetUtil {
  public static void resetReboot(SessionDataI sessionData) {
    log.debug("The reboot parameter is reset to 0 and the reboot will be executed");
    dbi.Unittype unittype = sessionData.getUnittype();
    dbi.UnittypeParameter utp = unittype.getUnittypeParameters().getByName(dbi.util.SystemParameters.RESTART);
    List<UnitParameter> unitParameters = new ArrayList<>();
    dbi.UnitParameter up = new dbi.UnitParameter(utp, sessionData.getUnitId(), "0", sessionData.getProfile());
    unitParameters.add(up);
    unitParameters.forEach(sessionData.getUnit()::toWriteQueue);
  }

  public static void resetReset(SessionDataI sessionData) {
    log.debug("The reset parameter is reset to 0 and the factory reset will be executed");
    dbi.Unittype unittype = sessionData.getUnittype();
    dbi.UnittypeParameter utp = unittype.getUnittypeParameters().getByName(dbi.util.SystemParameters.RESET);
    List<UnitParameter> unitParameters = new ArrayList<>();
    dbi.UnitParameter up = new dbi.UnitParameter(utp, sessionData.getUnitId(), "0", sessionData.getProfile());
    unitParameters.add(up);
    unitParameters.forEach(sessionData.getUnit()::toWriteQueue);
  }
}
