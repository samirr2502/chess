package service;

import model.AuthData;
import model.UserData;

public class LoginResult extends Result {
  private String username;
  private String authToken;
  private String errorMessage;
  public LoginResult(String username, String authToken) {
    this.username = username;
    this.authToken = authToken;
  }
  public void setErrorMessage(String message){
    errorMessage = message;
  }
  public String getErrorMessage(){
    return errorMessage;
  }
}
