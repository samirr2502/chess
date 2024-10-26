package dataaccess.authdao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
  @Override
  public AuthData getAuthDataByToken(String authToken) throws DataAccessException, SQLException {
//    try (var conn = DatabaseManager.getConnection()) {
//      var statement = "SELECT token FROM authdata WHERE token=authToken";
//      try (var ps = conn.prepareStatement(statement)) {
//
//        //ps.setInt(1, id);
//        try (var rs = ps.executeQuery()) {
//          if (rs.next()) {
//          //  return readPet(rs);
//          }
//        }
//      }
//    } catch (Exception e) {
//      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
//    }
    return null;
  }

  @Override
  public void addAuthData(AuthData authData) throws DataAccessException, SQLException {

  }

  @Override
  public void deleteAuthData(AuthData authData) throws DataAccessException, SQLException {

  }

  @Override
  public void deleteAllAuthData() throws DataAccessException, SQLException {

  }
}
