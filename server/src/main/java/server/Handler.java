package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SQLDataAccess;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.authdao.SQLAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.gamedao.SQLGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.SQLUserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.Service;
import service.results.*;
import spark.Request;
import spark.Response;

import java.sql.SQLException;


public class Handler {
  static DataAccess sqlDataAccess;

  static DataAccess memoryDataAccess;
  static Service SERVICE;

  static {

    try {
      sqlDataAccess = new SQLDataAccess(new SQLAuthDAO(), new SQLGameDAO(), new SQLUserDAO());

    } catch (SQLException | DataAccessException e) {
      throw new RuntimeException(e);
    }
    memoryDataAccess = new MemoryDataAccess(new MemoryAuthDAO(),new MemoryGameDAO(), new MemoryUserDAO());

    //Change the database here:
    SERVICE = new Service(sqlDataAccess);
  }

  private static final Gson JSON = new Gson();

  public static Object registerUser(Request req, Response res) {
    try {
      UserData registerUserRequest = JSON.fromJson(req.body(), UserData.class);
      String hashedPassword = BCrypt.hashpw(registerUserRequest.password(), BCrypt.gensalt());
      UserData hashedRegisterUserRequest = new UserData(registerUserRequest.username(),hashedPassword,registerUserRequest.email());
      String authToken = Service.generateToken();
      //Check if a field is not null
      if (registerUserRequest.password()==null|| hashedRegisterUserRequest.password() == null || hashedRegisterUserRequest.username() == null || hashedRegisterUserRequest.email() == null) {
        res.status(400);
        return JSON.toJson(new ErrorResult("Error: bad request"));
      }
      LoginResult registerUserResult = SERVICE.registerUser(hashedRegisterUserRequest, authToken);
      if (registerUserResult == null) {
        res.status(403);
        return JSON.toJson(new ErrorResult("Error: already taken"));
      } else {
        return JSON.toJson(registerUserResult); //200 Ok return
      }
    } catch (Exception ex) {
      return throwErrorException(res, ex);
    }
  }

  public static Object loginUser(Request req, Response res) {
    try {
      UserData loginUserRequest = JSON.fromJson(req.body(), UserData.class);
      String authToken = Service.generateToken();
      LoginResult loginResult = SERVICE.loginUser(loginUserRequest, authToken);

      if (loginResult != null) { //200 ok case
        return JSON.toJson(loginResult);
      } else {
        res.status(401);
        return JSON.toJson(new ErrorResult("Error: unauthorized"));
      }
    } catch (Exception ex) {
      return throwErrorException(res, ex);
    }
  }

  public static Object logoutUser(Request req, Response res) {
    try {
      String authToken = req.headers("authorization");
      AuthRequest logoutRequest = new AuthRequest(authToken);
      LogoutResult logoutResult = SERVICE.logoutUser(logoutRequest);
      if (logoutResult != null) { // 200 OK Case
        return "{}";
      } else {
        res.status(401);
        return JSON.toJson(new ErrorResult("Error: unauthorized"));
      }
    } catch (Exception ex) {
      return throwErrorException(res, ex);
    }
  }

  public static Object getGames(Request req, Response res) {
    try {
      String authToken = req.headers("authorization");
      AuthRequest logoutRequest = new AuthRequest(authToken);
      GetGamesResult getGameResult = SERVICE.getGames(logoutRequest);
      if (getGameResult != null) {
        return JSON.toJson(getGameResult);
      } else {
        res.status(401);
        return JSON.toJson(new ErrorResult("Error: unauthorized"));
      }
    } catch (Exception ex) {
      return throwErrorException(res, ex);
    }
  }

  public static Object createGame(Request req, Response res) {
    try {
      String authToken = req.headers("authorization");
      AuthRequest authRequest = new AuthRequest(authToken);

      CreateGameRequest createGameRequest = JSON.fromJson(req.body(), CreateGameRequest.class);
      CreateGameResult createGameResult = SERVICE.createGame(authRequest, createGameRequest);
      if (createGameResult!=null) {
        return JSON.toJson(createGameResult);
      } else{
        res.status(401);
        return JSON.toJson(new ErrorResult("Error: unauthorized"));
      }
    } catch (Exception ex) {
      return throwErrorException(res, ex);
    }
  }

  public static Object joinGame(Request req, Response res) {
    try {
      String authToken = req.headers("authorization");
      AuthRequest authRequest = new AuthRequest(authToken);
      JoinGameRequest joinGameRequest = JSON.fromJson(req.body(), JoinGameRequest.class);

      if (joinGameRequest.playerColor() == null) {
        res.status(400);
        return JSON.toJson(new ErrorResult("Error: bad request"));
      }

      JoinGameResult joinGameResult = SERVICE.joinGame(authRequest, joinGameRequest);

      if (joinGameResult != null && joinGameResult.gameData()!=null &&joinGameResult.colorAvailable() ) {
        return "{}";
      } else if(joinGameResult != null&& joinGameResult.gameData()!=null) {
        res.status(403);
        return JSON.toJson(new ErrorResult("Error: already taken"));
      }else if(joinGameResult!= null ) {
        res.status(400);
        return JSON.toJson(new ErrorResult("Error: bad request"));
      }else{
        res.status(401);
        return JSON.toJson(new ErrorResult("Error: unauthorized"));
      }
    } catch (Exception ex) {
      return throwErrorException(res, ex);
    }
  }

  public static Object clear(Request req, Response res) {
    try {
      ClearResult clearResult= SERVICE.clear();
      if(clearResult != null){
      return "{}";
      } else {
        throw new Exception("Service.clear is not working properly");
      }
    } catch (Exception ex) {
      return throwErrorException(res, ex);
    }
  }
  public static ErrorResult throwErrorException(Response res, Exception ex){
    res.status(500);
    return new ErrorResult("Error: "+ ex.getMessage());
  }
}
