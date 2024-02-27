package server;

import dataAccess.MemUserAccess;
import dataAccess.UserAccess;
import handler.ClearHandler;
import handler.RegisterHandler;
import service.DBService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final DBService dbService;
    //private final GameService gameService;
    private final UserService userService;
    private final ClearHandler clearHandler;
    private final RegisterHandler registerHandler;

    public Server() {
        // INITIALIZE DAOS
        UserAccess userDAO = new MemUserAccess();

        // INITIALIZE SERVICES
        dbService = new DBService(userDAO);
        userService = new UserService(userDAO);

        // INITIZIALIZE HANDLERS
        clearHandler = new ClearHandler(dbService);
        registerHandler = new RegisterHandler(userService);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) ->
                clearHandler.handle(req, res));

        Spark.post("/user", (req, res) ->
                registerHandler.handle(req, res));




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

