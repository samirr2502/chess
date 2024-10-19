package dataaccess.userdao;

import dataaccess.DataAccessException;
import model.UserData;

public class MemoryUserDAO implements UserDAO {

  @Override
  public UserData getUser(String username) throws DataAccessException {
      for(UserData userData:users){
        if(userData.username().equals( username)){
          return userData;
        }
      }
    return null;

  }

  @Override
  public void addUser(UserData user) {
    users.add(user);
  }

  @Override
  public void deleteAllUsers() {
    users.clear();
  }
}
