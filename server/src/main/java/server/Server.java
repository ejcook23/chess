package server;

import dataAccess.AuthAccess;
import dataAccess.MemAuthAccess;
import dataAccess.MemUserAccess;
import dataAccess.UserAccess;
import handler.ClearHandler;
import handler.LoginHandler;
import handler.LogoutHandler;
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
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;

    public Server() {
        // INIT DAOS
        UserAccess userDAO = new MemUserAccess();
        AuthAccess authDAO = new MemAuthAccess();

        // INIT SERVICES
        dbService = new DBService(userDAO, authDAO);
        userService = new UserService(userDAO, authDAO);

        // INIT HANDLERS
        clearHandler = new ClearHandler(dbService);
        registerHandler = new RegisterHandler(userService);
        loginHandler = new LoginHandler(userService);
        logoutHandler = new LogoutHandler(userService);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::handle);

        Spark.post("/user", registerHandler::handle);

        Spark.post("/session", loginHandler::handle);

        Spark.delete("/session", logoutHandler::handle);




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

