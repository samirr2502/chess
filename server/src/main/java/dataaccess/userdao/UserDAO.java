package dataaccess.userdao;

import model.UserData;

import java.util.ArrayList;

public interface UserDAO {
  ArrayList<UserData> users = new ArrayList<>();
  //Get user

  UserData getUser(String username) throws Exception;

  public void addUser(UserData user);

  public void deleteAllUsers();
}
