package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }
    private static void createRoutes() {
        Spark.post("/user", Handler::registerUser);

        Spark.post("/session",Handler::loginUser);
        Spark.delete("/session",Handler::logoutUser);

        Spark.get("/game", (req, res) -> "getGames");
        Spark.post("/game",(req,res)-> "CreatingGame");
        Spark.put("/game", (req, res) -> "JoiningGame");

        Spark.delete("/db",Handler::clear);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
