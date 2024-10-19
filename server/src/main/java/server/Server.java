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

        Spark.get("/game", Handler::getGames);
        Spark.post("/game",Handler::createGame);
        Spark.put("/game", Handler::joinGame);

        Spark.delete("/db",Handler::clear);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
