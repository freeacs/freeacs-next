package freeacs.dbi;

public enum ProvisioningProtocol {
    TR069,
    HTTP,
    OPP,
    NA,
    TFTP;

    public static ProvisioningProtocol toEnum(String s) {
        if (s == null || "TR-069".equals(s)) {
            return TR069;
        }
        if ("N/A".equals(s)) {
            return NA;
        }
        return valueOf(s);
    }
}
