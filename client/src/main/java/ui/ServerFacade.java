package ui;

import com.google.gson.Gson;
import com.sun.net.httpserver.Request;
import model.UserData;
import server.requests.AuthRequest;
import service.results.ClearResult;
import service.results.LoginResult;
import service.results.LogoutResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    return this.makeRequest("POST", path,null, user, LoginResult.class);
  }
  public LoginResult loginUser(UserData loginRequest) throws Exception {
    var path = "/session";
    return this.makeRequest("POST",path, null,loginRequest,LoginResult.class );
  }
  public LogoutResult logoutUser(AuthRequest logoutRequest) throws Exception {
    var path = "/session";
    return this.makeRequest("DELETE", path,logoutRequest.authToken(), logoutRequest, LogoutResult.class);
  }
  public void getGames(){

  }
  public void createGame(){
  }
  public void joinGame(){

  }
  public ClearResult clear() throws Exception {
    var path = "/db";
    return this.makeRequest("DELETE", path, null,null, ClearResult.class);
  }

  private <T> T makeRequest(String method, String path,String header, Object request, Class<T> result) throws Exception {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);
      writeBody(request, http);
      writeHeader(header, http);
      http.connect();
//      throwIfNotSuccessful(http);
      return readBody(http,result);
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }
  private static void writeHeader(String header, HttpURLConnection http) {
    if (header != null){
      http.addRequestProperty("authorization",header);
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
  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }

}


