import chess.*;
import server.Handler;
import server.Server;
import spark.Request;
import spark.Response;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        new Server().run(8080);
        createRoutes();;

    }
    private static void createRoutes() {
        //Examples
        Spark.get("/hello", (req, res) -> "Hello BYU!");
        Spark.delete("/goodbye",(req,res)-> "deletingBYU:ok");
        //Actual ones

        Spark.post("/user", Handler::registerUser);

        Spark.post("/session",(req, res) ->"Login");
        Spark.delete("/session",(req, res)->"logout");

        Spark.get("/game", (req, res) -> "Hello BYU!");
        Spark.post("/game",(req,res)->"CreatingGame");
        Spark.put("/game", (req, res) -> "JoiningGame");

        Spark.delete("/db",(req,res)-> "DeleteAll");
    }
    private static Object registerUser(Request req, Response res){
        return "registerUser";
    }
}