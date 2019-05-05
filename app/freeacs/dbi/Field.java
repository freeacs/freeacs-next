package freeacs.dbi;

public class Field {
    /** The database name of the column. */
    private String column;
    /**
     * The value to insert/update in the column. Could be NullString, NullInteger, any "normal" type
     * (String, Boolean, etc.) and null (in which case it will be skipped)
     */
    private Object value;
    /**
     * Set to true if this column is the primary key. If the value is infact null, the
     * prepared-statement will set it to auto-generated (retrieve it in a new query after the
     * insert).
     */
    private boolean primaryKey;

    public Field(String column, Object o) {
        this(column, o, false);
    }

    public Field(String column, Object o, boolean primaryKey) {
        this.column = column;
        this.value = o;
        this.primaryKey = primaryKey;
    }

    public Field(String column, Integer i) {
        this(column, i, false);
    }

    public Field(String column, Integer i, boolean primaryKey) {
        if (i != null) {
            this.value = i;
        } else {
            this.value = new DynamicStatement.NullInteger();
        }
        this.column = column;
        this.primaryKey = primaryKey;
    }

    public Field(String column, String s) {
        this(column, s, false);
    }

    public Field(String column, String s, boolean primaryKey) {
        if (s != null) {
            this.value = s;
        } else {
            this.value = new DynamicStatement.NullString();
        }
        this.column = column;
        this.primaryKey = primaryKey;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean equals(Object o) {
        if (o instanceof Field) {
            Field f = (Field) o;
            return f.getColumn().equals(getColumn());
        }
        return false;
    }

    public String toString() {
        return column;
    }
}
