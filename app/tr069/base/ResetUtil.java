package tr069.base;

import com.github.freeacs.dbi.UnitParameter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ResetUtil {
  public static void resetReboot(SessionDataI sessionData) {
    log.debug("The reboot parameter is reset to 0 and the reboot will be executed");
    com.github.freeacs.dbi.Unittype unittype = sessionData.getUnittype();
    com.github.freeacs.dbi.UnittypeParameter utp = unittype.getUnittypeParameters().getByName(com.github.freeacs.dbi.util.SystemParameters.RESTART);
    List<UnitParameter> unitParameters = new ArrayList<>();
    com.github.freeacs.dbi.UnitParameter up = new com.github.freeacs.dbi.UnitParameter(utp, sessionData.getUnitId(), "0", sessionData.getProfile());
    unitParameters.add(up);
    unitParameters.forEach(sessionData.getUnit()::toWriteQueue);
  }

  public static void resetReset(SessionDataI sessionData) {
    log.debug("The reset parameter is reset to 0 and the factory reset will be executed");
    com.github.freeacs.dbi.Unittype unittype = sessionData.getUnittype();
    com.github.freeacs.dbi.UnittypeParameter utp = unittype.getUnittypeParameters().getByName(com.github.freeacs.dbi.util.SystemParameters.RESET);
    List<UnitParameter> unitParameters = new ArrayList<>();
    com.github.freeacs.dbi.UnitParameter up = new com.github.freeacs.dbi.UnitParameter(utp, sessionData.getUnitId(), "0", sessionData.getProfile());
    unitParameters.add(up);
    unitParameters.forEach(sessionData.getUnit()::toWriteQueue);
  }
}
