package database.userdao;

import model.UserData;

public class MemoryUserDAO implements UserDAO {

  @Override
  public UserData getUser(String username) {
    for (UserData userData : USERS) {
      if (userData.username().equals(username)) {
        return userData;
      }
    }
    return null;
  }

  @Override
  public void addUser(UserData user) {
    USERS.add(user);
  }

  @Override
  public void deleteAllUsers() {
    USERS.clear();
  }
}
