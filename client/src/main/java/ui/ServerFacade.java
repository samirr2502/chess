package ui;

import com.google.gson.Gson;
import model.UserData;
import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.results.*;

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
  public GetGamesResult getGames(AuthRequest getGamesRequest) throws Exception {
    var path ="/game";
    return this.makeRequest("GET", path, getGamesRequest.authToken(), getGamesRequest, GetGamesResult.class);
  }
  public CreateGameResult createGame(AuthRequest authRequest, CreateGameRequest createGameRequest) throws Exception {
    var path = "/game";
    return this.makeRequest("POST",path, authRequest.authToken(), createGameRequest, CreateGameResult.class);
  }
  public JoinGameResult joinGame(AuthRequest authRequest, JoinGameRequest joinGameRequest) throws Exception {
    var path ="/game";
    return this.makeRequest("PUT",path, authRequest.authToken(), joinGameRequest, JoinGameResult.class);
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
      http.setDoOutput(!method.equals("GET"));
      writeHeader(header, http);
      writeBody(request, http);
      http.connect();
//      throwIfNotSuccessful(http);
      int responseCode = http.getResponseCode();

      if (responseCode == HttpURLConnection.HTTP_OK) { // 200
        //System.out.println("Request successful");
        // Process the response (e.g., read input stream)
      } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) { // 400
        throw new Exception("Bad request");
        // Handle error (e.g., read error stream)
      } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) { // 401
        throw new Exception("Unauthorized");

        // Handle unauthorized access
      }  else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) { // 401
        throw new Exception("Already taken");
        // Handle unauthorized access
      } else {
        throw new Exception("Internal Error");
        // Handle other status codes
      }
      return readBody(http,result);
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }
  private static void writeHeader(String header, HttpURLConnection http) {
    if (header != null){
      http.setRequestProperty("authorization",header);
    }
  }
  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null && http.getDoOutput()) {
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


