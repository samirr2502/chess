package dataaccess.authdao;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

  @Override
  public AuthData getAuthDataByToken(String authToken) {
    for (AuthData authData : AUTH_DATA_LIST) {
      if (authData.authToken().equals(authToken)) {
        return authData;
      }
    }
    return null;
  }

  @Override
  public void addAuthData(AuthData authData) {
    AUTH_DATA_LIST.add(authData);
  }

  @Override
  public void deleteAuthData(AuthData authData) {
    AUTH_DATA_LIST.remove(authData);
  }

  @Override
  public void deleteAllAuthData() {
    AUTH_DATA_LIST.clear();
  }
}
