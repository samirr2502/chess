import chess.*;
import server.Handler;
import server.Server;
import spark.Request;
import spark.Response;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        new Server().run(8080);

    }

}