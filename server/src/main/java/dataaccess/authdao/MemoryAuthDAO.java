package dataaccess.authdao;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

  @Override
  public AuthData getAuthDataByToken(String authToken) {
    for (AuthData authData : authDataList) {
      if (authData.authToken().equals(authToken)) {
        return authData;
      }
    }
    return null;
  }

  @Override
  public void addAuthData(AuthData authData) {
    authDataList.add(authData);
  }

  @Override
  public void deleteAuthData(AuthData authData) {
    authDataList.remove(authData);
  }

  @Override
  public void deleteAllAuthData() {
    authDataList.clear();
  }
}
