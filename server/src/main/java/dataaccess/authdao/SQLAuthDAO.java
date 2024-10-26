package dataaccess.authdao;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
  @Override
  public AuthData getAuthDataByToken(String authToken) throws DataAccessException, SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT json FROM authData WHERE token=?";
      var ps = conn.prepareStatement(statement);
        ps.setString(1, authToken);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            String json = rs.getString("json");
            return new Gson().fromJson(json, AuthData.class);
          }
          return null;
        }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }

  @Override
  public void addAuthData(AuthData authData) throws DataAccessException, SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "INSERT INTO authData (token, username, json) values (?,?,?)";
      var json = new Gson().toJson(authData);

      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1,authData.authToken());
        ps.setString(2,authData.username());
        ps.setString(3,json);
        ps.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }
  @Override
  public void deleteAuthData(AuthData authData) throws DataAccessException, SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "DELETE FROM authData WHERE token=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1,authData.authToken());
        ps.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }

  @Override
  public void deleteAllAuthData() throws DataAccessException, SQLException {
    //executeUpdate(statement);
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "TRUNCATE authData";
      try (var ps = conn.prepareStatement(statement)) {
        ps.executeUpdate();
      } catch (Exception e) {
        throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
      }
    }
  }
}
