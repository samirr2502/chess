package database.authdao;

import database.DataAccessException;
import model.AuthData;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AuthDAO {
  ArrayList<AuthData> AUTH_DATA_LIST = new ArrayList<>();
  AuthData getAuthDataByToken(String authToken) throws DataAccessException, SQLException;
  void addAuthData(AuthData authData) throws DataAccessException, SQLException;
  void deleteAuthData(AuthData authData) throws DataAccessException, SQLException;
  void deleteAllAuthData() throws DataAccessException, SQLException;
}
