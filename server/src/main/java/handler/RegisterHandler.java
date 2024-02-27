package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import model.AuthData;
import model.ErrorMsg;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class RegisterHandler {

    UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {

        // this is helpful for others, not needed for Register
        // String headers = req.headers("authorization");

        Gson json = new Gson();
        try {
            UserData userData = json.fromJson(req.body(), UserData.class);
            // pass this information to the service
            AuthData authData = userService.register(userData);
            // serialize and add to response
            res.body(json.toJson(authData));
            res.status(200);

        } catch (DataAccessException e) {
            // if the error message equals... set to corresponding response and code
            if(Objects.equals(e.getMessage(), "Error: already taken")) {
                res.body(json.toJson(new ErrorMsg(e.getMessage())));
                res.status(403);
            }
            if(Objects.equals(e.getMessage(), "Error: bad request")) {
                res.body(json.toJson(new ErrorMsg(e.getMessage())));
                res.status(400);
            }
            else {
                res.body(json.toJson(new ErrorMsg("Error: unknown error, see RegisterHandler.java")));
                res.status(500);
            }

        }

        return res.body();
    }
}
