package dataaccess.authdao;

import model.AuthData;

import java.util.ArrayList;

public interface AuthDAO {
  ArrayList<AuthData> authDataList = new ArrayList<>();

  public AuthData getAuthData(String username);
  public void addAuthData(AuthData authData);
  public void getAllAuthData();
  public void deleteAuthData(AuthData authData);
  public void deleteAllAuthData();
}
