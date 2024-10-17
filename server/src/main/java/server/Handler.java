package server;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class Handler {
  private Object registerUser(Request req, Response res) throws Exception {
//   var pet = new Gson().fromJson(req.body(), User.class);
//    pet = Service.(pet);
    //webSocketHandler.makeNoise(pet.name(), pet.sound());
    return new Gson().toJson("user");
  }
}
