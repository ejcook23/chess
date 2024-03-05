package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.ErrorMsg;
import model.JoinGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Objects;


public class JoinGameHandler {

    GameService gameService;

    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object handle(Request req, Response res) {

        Gson json = new Gson();

        try {
            String header = req.headers("Authorization");
            System.out.println(header);

            JoinGameRequest request = json.fromJson(req.body(), JoinGameRequest.class);

            gameService.joinGame(request, header);
            res.body("{}");
            res.status(200);

        } catch (DataAccessException e) {
            // if the error message equals... set to corresponding response and code
            res.body(json.toJson(new ErrorMsg(e.getMessage())));

            if(Objects.equals(e.getMessage(), "Error: bad request")) {
                res.status(400);
            }
            else if(Objects.equals(e.getMessage(), "Error: unauthorized")) {
                res.status(401);
            }
            else if(Objects.equals(e.getMessage(), "Error: already taken")) {
                res.status(403);
            }

            else {
                res.body(json.toJson(new ErrorMsg("Error: ")));
                res.status(500);
            }

        }

        return res.body();
    }


}

