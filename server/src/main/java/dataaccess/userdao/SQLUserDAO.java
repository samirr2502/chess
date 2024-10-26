package dataaccess.userdao;

import dataaccess.DataAccessException;
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

  }
}
