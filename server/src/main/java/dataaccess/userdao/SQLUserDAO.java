package dataaccess.userdao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
  @Override
  public UserData getUser(String username) throws DataAccessException, SQLException {
    return null;
  }

  @Override
  public void addUser(UserData user) throws DataAccessException, SQLException {

  }

  @Override
  public void deleteAllUsers() throws DataAccessException, SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "TRUNCATE userData";
      try (var ps = conn.prepareStatement(statement)) {
        ps.executeUpdate();
      } catch (Exception e) {
        throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
      }
    }
  }
}
