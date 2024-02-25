package handler;

import passoffTests.testClasses.TestModels;
import service.DBService;
import service.GameService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public Object handle(Request req, Response res) {
        // clear games
        // clear users
        // clear authtokens
        return req.body();
    }
    // method that takes in a spark request and response (spark objects to read from and write to)
    // parse and deserialize (if needed)
    // calls service method
    // serialize return object into the body (return Gson string from serialization and write whatever you need to the response object (status))
}

