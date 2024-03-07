package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.CreateGameRequest;
import model.ErrorMsg;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class CreateGameHandler {

    GameService gameService;

    public CreateGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    // THE REST OF THIS HANDLER IS NOT IMPLEMENTED CORRECTLY AND IS A COPY
    public Object handle(Request req, Response res) {


        Gson json = new Gson();

        try {
            // Get the AUTH TOKEN from the header
            String header = req.headers("Authorization");
            CreateGameRequest gameRequest = json.fromJson(req.body(), CreateGameRequest.class);
            System.out.println(gameRequest.gameName());
            System.out.println(header);
            // create the game and return the new gameID
            res.body(json.toJson(gameService.createGame(header, gameRequest.gameName())));
            res.status(200);

        } catch (Exception e) {
            // if the error message equals... set to corresponding response and code
            res.body(json.toJson(new ErrorMsg(e.getMessage())));

            if(Objects.equals(e.getMessage(), "Error: bad request")) {
                res.status(400);
            }
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

