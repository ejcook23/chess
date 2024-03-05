package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.CreateGameRequest;
import model.ErrorMsg;
import model.ListGamesResponse;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Objects;


public class ListGamesHandler {

    GameService gameService;

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object handle(Request req, Response res) {

        Gson json = new Gson();


        try {
            // Get the AUTH TOKEN from the header
            String header = req.headers("Authorization");
            System.out.println(header);
            // create the game and return the new gameID
            ListGamesResponse response = gameService.listGames(header);
            res.body(json.toJson(response));
            System.out.println(response);

            res.status(200);

        } catch (DataAccessException e) {
            // if the error message equals... set to corresponding response and code
            res.body(json.toJson(new ErrorMsg(e.getMessage())));

            if(Objects.equals(e.getMessage(),"Error: unauthorized")) {
                res.status(401);
            }
            else {
                res.body(json.toJson(new ErrorMsg("Error: ")));
                res.status(500);
            }

        }

        return res.body();
    }


}

