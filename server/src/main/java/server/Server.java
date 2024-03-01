package server;

import dataAccess.*;
import handler.*;
import service.DBService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final DBService dbService;
    //private final GameService gameService;
    private final UserService userService;
    private final GameService gameService;
    private final ClearHandler clearHandler;
    private final RegisterHandler registerHandler;
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;
    private final CreateGameHandler createGameHandler;

    public Server() {
        // INIT DAOS
        UserAccess userDAO = new MemUserAccess();
        AuthAccess authDAO = new MemAuthAccess();
        GameAccess gameDAO = new MemGameAccess();
        // INIT SERVICES
        dbService = new DBService(userDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(authDAO, gameDAO);

        // INIT HANDLERS
        clearHandler = new ClearHandler(dbService);
        registerHandler = new RegisterHandler(userService);
        loginHandler = new LoginHandler(userService);
        logoutHandler = new LogoutHandler(userService);
        createGameHandler = new CreateGameHandler(gameService);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::handle);

        Spark.post("/user", registerHandler::handle);

        Spark.post("/session", loginHandler::handle);

        Spark.delete("/session", logoutHandler::handle);

        Spark.post("/game", createGameHandler::handle);

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

