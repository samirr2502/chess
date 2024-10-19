package server;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import model.UserData;
import service.ErrorResult;
import service.JoinGameRequest;
import service.Result;
import service.Service;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.security.spec.ECField;

public class Handler  {
  private static final Service service = new Service();
  private static final MemoryUserDAO memoryUserDAO= new MemoryUserDAO();
  private static final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
  private static final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
  private static final Gson json= new Gson();
  public static Object registerUser(Request req, Response res) throws Exception {
    try {
      UserData user = json.fromJson(req.body(), UserData.class);
      String response = json.toJson(service.registerUser(res, user, memoryUserDAO, memoryAuthDAO));
      System.out.println(response);
      return response;
    } catch (Exception ex){
      res.status(500);
      String response = json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
      System.out.println(response);
      return response;
    }
  }
  public static Object loginUser(Request req, Response res) throws Exception{
    try {
      UserData user = json.fromJson(req.body(), UserData.class);
      String response = json.toJson(service.loginUser(res, user, memoryUserDAO, memoryAuthDAO));
      System.out.println(response);
      return response;

    } catch (Exception ex){
      res.status(500);
      String response = json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
      System.out.println(response);
      return response;
    }
  }

  public static Object logoutUser(Request req, Response res) throws Exception{
    try {
      String response;
      String authToken = req.headers("authorization");
      AuthRequest logoutRequest= new AuthRequest(authToken);
      Result logoutResult= service.logoutUser(res,logoutRequest,memoryAuthDAO);
      if(logoutResult== null){
        return "{}";
      } else{
        response =json.toJson(logoutResult);
      }
      System.out.println(response);
      return response;
    } catch (Exception ex){
      res.status(500);
      String response = json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
      System.out.println(response);
      return response;
    }
  }

  public static Object getGames(Request req, Response res) throws Exception{
    try {
      String authToken = req.headers("authorization");
      AuthRequest logoutRequest= new AuthRequest(authToken);
      Result getGameResult = service.getGames(res,logoutRequest,memoryGameDAO, memoryAuthDAO);
      return json.toJson(getGameResult);
    } catch (Exception ex){
      res.status(500);
      return json.toJson(new ErrorResult(STR."Error \{ex.getMessage()}"));
    }
  }
  public static Object createGame(Request req, Response res) throws Exception{
    try {
      String authToken = req.headers("authorization");

      CreateGameRequest createGameRequest= json.fromJson(req.body(),CreateGameRequest.class);
      Result createGameResult= service.createGame(res, createGameRequest, authToken,memoryGameDAO, memoryAuthDAO);
      return json.toJson(createGameResult);
    } catch (Exception ex){
      return json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
    }
  }
  public static Object joinGame(Request req, Response res) throws Exception{
    try {
      String authToken = req.headers("authorization");
      JoinGameRequest joinGameRequest= json.fromJson(req.body(),JoinGameRequest.class);
      Result joinGameResult = service.joinGame(res,joinGameRequest,authToken,memoryAuthDAO,memoryGameDAO);
      if(joinGameResult== null){
        return "{}";
      } else{
        //res.status(400);
        return json.toJson(joinGameResult);
      }
    } catch(Exception ex){
      res.status(500);
      return json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));

    }
  }

  public static Object clear(Request req, Response res) {
    service.clear(memoryAuthDAO, memoryUserDAO, memoryGameDAO);
    return "{}";
  }

}
