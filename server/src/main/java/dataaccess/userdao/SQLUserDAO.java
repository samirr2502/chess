package dataaccess.userdao;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
  @Override
  public UserData getUser(String username) throws SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT json FROM userData WHERE username=?";
      var ps = conn.prepareStatement(statement);
      ps.setString(1, username);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          String json = rs.getString("json");
          return new Gson().fromJson(json, UserData.class);
        }
        return null;
      }
    } catch (Exception e) {
      throw new SQLException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }

  @Override
  public void addUser(UserData user) throws SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "INSERT INTO userData (username, password, email,json) values (?,?,?,?)";
      var json = new Gson().toJson(user);
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1,user.username());
        ps.setString(2,user.password());
        ps.setString(3,user.email());
        ps.setString(4,json);
        ps.executeUpdate();
      }
    } catch (Exception e) {
      throw new SQLException(String.format("Unable to read data: %s", e.getMessage()));
    }
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
