package freeacs.dbi;

public enum ParameterDataType {
    TEXT("TEXT"),
    NUMBER("NUMBER");
    private String type;

    ParameterDataType(String type) {
        this.type = type;
    }

    public static ParameterDataType getDataType(String type) {
        if ("TEXT".equals(type)) {
            return TEXT;
        }
        if ("NUMBER".equals(type)) {
            return NUMBER;
        }
        throw new IllegalArgumentException("Data type " + type + " is not a valid type");
    }

    public String getType() {
        return this.type;
    }

    public String getSQL() {
        if (TEXT.equals(this)) {
            return "?";
        } else {
            return "CONVERT(?,SIGNED)";
        }
    }
}
