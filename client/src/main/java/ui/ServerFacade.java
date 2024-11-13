package ui;

import com.google.gson.Gson;
import com.sun.net.httpserver.Request;
import model.UserData;
import service.results.LoginResult;
import service.results.LogoutResult;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
  String serverUrl;
  public ServerFacade(String serverUrl){
    this.serverUrl =  serverUrl;
//    Spark.post("/user", Handler::registerUser);
//    Spark.post("/session",Handler::loginUser);
//    Spark.delete("/session",Handler::logoutUser);
//
//    Spark.get("/game", Handler::getGames);
//    Spark.post("/game",Handler::createGame);
//    Spark.put("/game", Handler::joinGame);
//
//    Spark.delete("/db",Handler::clear);

  }
  public LoginResult registerUser(UserData user) throws Exception {
    var path = "/user";

    return this.makeRequest("POST", path, user, LoginResult.class);
  }
  public void loginUser(){

  }
  public void logoutUser(){

  }
  public void getGames(){

  }
  public void createGame(){
  }
  public void joinGame(){

  }
  public void clear(){

  }

  private <T> T makeRequest(String method, String path, Object request, Class<T> result) throws Exception {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);

//      writeBody(request, http);
//      http.connect();
//      throwIfNotSuccessful(http);
        return null;
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }
  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }
}


