package server;

import com.google.gson.Gson;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import model.UserData;
import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.Service;
import service.results.*;
import spark.Request;
import spark.Response;


public class Handler {
  private static final Service service = new Service();
  private static final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
  private static final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
  private static final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
  private static final Gson json = new Gson();

  public static Object registerUser(Request req, Response res) {
    try {
      UserData registerUserRequest = json.fromJson(req.body(), UserData.class);
      String authToken = Service.generateToken();
      LoginResult registerUserResult = service.registerUser(registerUserRequest, authToken, memoryUserDAO, memoryAuthDAO);

      //Check if a field is not null
      if (registerUserRequest.password() == null || registerUserRequest.username() == null || registerUserRequest.email() == null) {
        res.status(400);
        return json.toJson(new ErrorResult("Error: bad request"));
      } else if (registerUserResult == null) {
        res.status(403);
        return json.toJson(new ErrorResult("Error: already taken"));
      } else {
        return json.toJson(registerUserResult); //200 Ok return
      }
    } catch (Exception ex) {
      res.status(500);
      return json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
    }
  }

  public static Object loginUser(Request req, Response res) {
    try {
      UserData loginUserRequest = json.fromJson(req.body(), UserData.class);
      String authToken = Service.generateToken();
      LoginResult loginResult = service.loginUser(loginUserRequest, authToken, memoryUserDAO, memoryAuthDAO);

      if (loginResult != null) { //200 ok case
        return json.toJson(loginResult);
      } else {
        res.status(401);
        return json.toJson(new ErrorResult("Error: unauthorized"));
      }
    } catch (Exception ex) {
      res.status(500);
      return json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
    }
  }

  public static Object logoutUser(Request req, Response res) {
    try {
      String authToken = req.headers("authorization");
      AuthRequest logoutRequest = new AuthRequest(authToken);
      LogoutResult logoutResult = service.logoutUser(logoutRequest, memoryAuthDAO);
      if (logoutResult != null) { // 200 OK Case
        return "{}";
      } else {
        res.status(401);
        return json.toJson(new ErrorResult("Error: unauthorized"));
      }
    } catch (Exception ex) {
      res.status(500);
      String response = json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
      System.out.println(response);
      return response;
    }
  }

  public static Object getGames(Request req, Response res) {
    try {
      String authToken = req.headers("authorization");
      AuthRequest logoutRequest = new AuthRequest(authToken);
      Result getGameResult = service.getGames(logoutRequest, memoryGameDAO, memoryAuthDAO);
      if (getGameResult != null) {
        return json.toJson(getGameResult);
      } else {
        res.status(401);
        return json.toJson(new ErrorResult("Error: unauthorized"));
      }
    } catch (Exception ex) {
      res.status(500);
      return json.toJson(new ErrorResult(STR."Error \{ex.getMessage()}"));
    }
  }

  public static Object createGame(Request req, Response res) {
    try {
      String authToken = req.headers("authorization");
      AuthRequest authRequest = new AuthRequest(authToken);

      CreateGameRequest createGameRequest = json.fromJson(req.body(), CreateGameRequest.class);
      Result createGameResult = service.createGame(authRequest, createGameRequest, memoryGameDAO, memoryAuthDAO);
      if (createGameResult!=null) {
        return json.toJson(createGameResult);
      } else{
        res.status(401);
        return json.toJson(new ErrorResult("Error: unauthorized"));
      }
    } catch (Exception ex) {
      res.status(500);
      return json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
    }
  }

  public static Object joinGame(Request req, Response res) {
    try {
      String authToken = req.headers("authorization");
      AuthRequest authRequest = new AuthRequest(authToken);
      JoinGameRequest joinGameRequest = json.fromJson(req.body(), JoinGameRequest.class);

      if (joinGameRequest.playerColor() == null) {
        res.status(400);
        return json.toJson(new ErrorResult("Error: bad request"));
      }

      JoinGameResult joinGameResult = service.joinGame(authRequest, joinGameRequest, memoryAuthDAO, memoryGameDAO);

      if (joinGameResult != null && joinGameResult.gameData()!=null &&joinGameResult.colorAvailable() ) {
        return "{}";
      } else if(joinGameResult != null&& joinGameResult.gameData()!=null) {
        res.status(403);
        return json.toJson(new ErrorResult("Error: already taken"));
      }else if(joinGameResult!= null ) {
        res.status(400);
        return json.toJson(new ErrorResult("Error: bad request"));
      }else{
        res.status(401);
        return json.toJson(new ErrorResult("Error: unauthorized"));
      }
    } catch (Exception ex) {
      res.status(500);
      return json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
    }
  }

  public static Object clear(Request req, Response res) {
    try {
      service.clear(memoryAuthDAO, memoryUserDAO, memoryGameDAO);
      return "{}";
    } catch (Exception ex) {
      res.status(500);
      return new ErrorResult(STR."Error: \{ex.getMessage()}");
    }
  }

}
