package server;

import handler.ClearHandler;
import handler.RegisterHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) ->
                (new ClearHandler()).handle(req, res));

        Spark.post("/user", (req, res) ->
                (new RegisterHandler()).handle(req, res));




        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void main(String[] args) {

        Server sparkServer = new Server();
        sparkServer.run(8080);

    }
}
