package freeacs.dbi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper class to easily build SQL for insert/update, since it's the same columns
 * involved. This class is a wrapper class around DynamicStatement, which is more generic in it's
 * approach to SQL generation.
 *
 * @author Morten
 */
public class InsertOrUpdateStatement {

  private List<Field> fields = new ArrayList<>();
  private String table;
  private boolean insert;

  public InsertOrUpdateStatement(String table, Field primaryKey) {
    this.table = table;
    primaryKey.setPrimaryKey(true);
    fields.add(primaryKey);
  }

  public void addField(Field field) {
    fields.add(field);
  }

  /**
   * This method will either make an insert or and update. If primary keys are null, it will make an
   * insert (the primary keys will be auto-generated), otherwise it will make an update.
   *
   * @param c
   * @return
   * @throws SQLException
   */
  public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
    List<String> autoGeneratePK = new ArrayList<>();
    List<Field> updateKeys = new ArrayList<>();
    for (Field field : fields) {
      if (field.isPrimaryKey()) {
        if (field.getValue() == null
            || field.getValue() instanceof DynamicStatement.NullString
            || field.getValue() instanceof DynamicStatement.NullInteger) {
          insert = true;
          autoGeneratePK.add(field.getColumn());
        } else {
          updateKeys.add(field);
        }
      }
    }
    DynamicStatement ds = new DynamicStatement();
    if (insert) {
      ds.setSql("INSERT INTO " + table + " (");
      for (Field field : fields) {
        if (!autoGeneratePK.contains(field.getColumn())) {
          ds.addSqlAndArguments(field.getColumn() + ", ", field.getValue());
        }
      }
      ds.cleanupSQLTail();
      ds.addSql(") VALUES (" + ds.getQuestionMarks() + ")");
      String[] primaryKeyStrArr = autoGeneratePK.toArray(new String[] {});
      return ds.makePreparedStatement(c, primaryKeyStrArr);
    } else { // update
      ds.setSql("UPDATE " + table + " SET ");
      for (Field field : fields) {
        if (!updateKeys.equals(field)) {
          ds.addSqlAndArguments(field.getColumn() + " = ?, ", field.getValue());
        }
      }
      ds.cleanupSQLTail();
      ds.addSql(" WHERE ");
      for (Field field : updateKeys) {
        ds.addSqlAndArguments(field.getColumn() + " = ?, ", field.getValue());
      }
      ds.cleanupSQLTail();
      return ds.makePreparedStatement(c);
    }
  }

  public boolean isInsert() {
    return insert;
  }
}
