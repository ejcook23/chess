package server;

import dataAccess.AuthAccess;
import dataAccess.MemAuthAccess;
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
        // INIT DAOS
        UserAccess userDAO = new MemUserAccess();
        AuthAccess authDAO = new MemAuthAccess();

        // INIT SERVICES
        dbService = new DBService(userDAO);
        userService = new UserService(userDAO, authDAO);

        // INIT HANDLERS
        clearHandler = new ClearHandler(dbService);
        registerHandler = new RegisterHandler(userService);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::handle);

        Spark.post("/user", registerHandler::handle);




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

