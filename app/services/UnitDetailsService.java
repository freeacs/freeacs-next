package services;

import freeacs.dbi.ACSUnit;
import freeacs.dbi.DBI;
import freeacs.dbi.DBIHolder;
import freeacs.dbi.Unit;
import freeacs.dbi.util.SystemParameters;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;

@Singleton
public class UnitDetailsService {

    private final DBI dbi;

    @Inject()
    public UnitDetailsService(DBIHolder dbi) {
        this.dbi = dbi.dbi();
    }

    public UnitDetails loadUserByUsername(String username) {
        try {
            ACSUnit acsUnit = dbi.getACSUnit();
            Unit unit = acsUnit.getUnitById(username);
            if (unit != null) {
                String secret = unit.getUnitParameters().get(SystemParameters.SECRET).getValue();
                if (secret == null) {
                    throw new IllegalArgumentException("User " + username + " has no secret");
                }
                return UnitDetails.apply(username, secret);
            } else {
                throw new IllegalArgumentException("User was not found: " + username);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to retrieve user: " + username, e);
        }
    }
}
