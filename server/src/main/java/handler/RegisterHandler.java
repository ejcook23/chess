package handler;

import com.google.gson.Gson;
import model.UserData;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    public Object handle(Request req, Response res) {

        // this is helpful for others, not needed for Register
        String headers = req.headers("authorization");

        Gson json = new Gson();

        UserData userData = json.fromJson(req.body(), UserData.class);
        // pass this information to the service



    }
}
