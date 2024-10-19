package server;
import com.google.gson.JsonObject;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import service.ErrorResult;
import service.Service;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class Handler  {
  private static final Service service = new Service();
  private static final MemoryUserDAO memoryUserDAO= new MemoryUserDAO();
  private static final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
  private static final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
  private static final Gson json= new Gson();
  public static Object registerUser(Request req, Response res) throws Exception {
    //System.out.println(req.body());
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
    //System.out.println(req.body());
    try {
      UserData user = json.fromJson(req.body(), UserData.class);
      String response = json.toJson(service.loginUser(res, user, memoryUserDAO, memoryAuthDAO));
      System.out.println(response);
      return response;
      //test
      // return "{\"name\":\"samir\"}";
    } catch (Exception ex){
      res.status(500);
      String response = json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
      System.out.println(response);
      return response;
    }
  }

  public static Object logoutUser(Request req, Response res) throws Exception{
    //System.out.println(req.body());
    try {
      String response;
      String authToken = req.headers("authorization");
      LogoutRequest logoutRequest= new LogoutRequest(authToken);
      if(service.logoutUser(res,logoutRequest,memoryAuthDAO) == null){
        response="{}";
      } else{
        response =json.toJson(service.logoutUser(res, logoutRequest, memoryAuthDAO));
      }
      System.out.println(response);
      return response;
      //test
      // return "{\"name\":\"samir\"}";
    } catch (Exception ex){
      res.status(500);
      String response = json.toJson(new ErrorResult(STR."Error: \{ex.getMessage()}"));
      System.out.println(response);
      return response;
    }
  }

  public static Object clear(Request req, Response res) {
    service.clear(memoryAuthDAO, memoryUserDAO, memoryGameDAO);
    return "{}";
  }

}
