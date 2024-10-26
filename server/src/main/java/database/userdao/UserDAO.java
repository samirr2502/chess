package database.userdao;

import database.DataAccessException;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserDAO {
  ArrayList<UserData> USERS = new ArrayList<>();
  //Get user

  UserData getUser(String username) throws DataAccessException, SQLException;
  void addUser(UserData user) throws DataAccessException, SQLException;
  void deleteAllUsers() throws DataAccessException, SQLException;
}
