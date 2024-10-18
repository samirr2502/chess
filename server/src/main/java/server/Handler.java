package server;
import service.UserService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

public class Handler  {
  public static Object registerUser(Request req, Response res) throws Exception {
    UserService service= new UserService();
    service.registerUser();
    return "{\"name\":\"samir\"}";
  }

}
