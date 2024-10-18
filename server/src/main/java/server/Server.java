package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
//        Spark.post("user", new Route() {
//            @Override
//            public Object handle(Request request, Response response) throws Exception {
//                return null;
//            }
//        });
//        // Register your endpoints and handle exceptions here.
//        Spark.get("/hello", (req, res) -> "Hello World");
//        //This line initializes the server and can be removed once you have a functioning endpoint

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
