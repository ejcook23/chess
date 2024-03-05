package handler;

import com.google.gson.Gson;
import service.GameService;
import spark.Request;
import spark.Response;


public class JoinGameHandler {

    GameService gameService;

    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object handle(Request req, Response res) {

        Gson json = new Gson();

        return res.body();
    }


}

