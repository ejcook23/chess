package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.ErrorMsg;
import model.LoginRequest;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class LoginHandler {

    UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {

        // this is helpful for others, not needed for Register
        // String headers = req.headers("authorization");

        Gson json = new Gson();

        try {
            // DESERIALIZE data into Json UserData
            LoginRequest userData = json.fromJson(req.body(), LoginRequest.class);
            // Pass userData to the service, register, and pass back the body to be JSON'd
            res.body(json.toJson(userService.login(userData)));
            res.status(200);

        } catch (DataAccessException e) {
            // if the error message equals... set to corresponding response and code
            res.body(json.toJson(new ErrorMsg(e.getMessage())));

            if(Objects.equals(e.getMessage(), "Error: unauthorized")) {
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
