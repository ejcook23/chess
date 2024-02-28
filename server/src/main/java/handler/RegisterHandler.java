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
            // DESERIALIZE data into Json UserData
            UserData userData = json.fromJson(req.body(), UserData.class);
            // Pass userData to the service, register, and pass back the body to be JSON'd
            res.body(json.toJson(userService.register(userData)));
            res.status(200);

        } catch (DataAccessException e) {
            // if the error message equals... set to corresponding response and code
            res.body(json.toJson(new ErrorMsg(e.getMessage())));

            if(Objects.equals(e.getMessage(), "Error: already taken")) {
                res.status(403);
            }
            else if(Objects.equals(e.getMessage(), "Error: bad request")) {
                res.status(400);
            }
            else {
                res.body(json.toJson(new ErrorMsg("Error: ")));
                res.status(500);
            }

        }

        return res.body();
    }
}
