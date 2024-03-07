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

public class LogoutHandler {

    UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {


        Gson json = new Gson();

        try {
            // Get the AUTH TOKEN from the header
            String header = req.headers("Authorization");
            System.out.println(header);
            // logout using the header
            userService.logout(header);
            res.body("{}");
            res.status(200);

        } catch (Exception e) {
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

