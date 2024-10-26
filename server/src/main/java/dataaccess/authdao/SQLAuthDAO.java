package dataaccess.authdao;

import dataaccess.DataAccessException;
import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
  @Override
  public AuthData getAuthDataByToken(String authToken) throws DataAccessException, SQLException {
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
