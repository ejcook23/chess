package handler;

import com.google.gson.Gson;
import service.GameService;
import spark.Request;
import spark.Response;



public class ListGamesHandler {

    GameService gameService;

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object handle(Request req, Response res) {

        Gson json = new Gson();

        return res.body();
    }


}

