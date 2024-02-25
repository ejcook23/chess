package handler;

import dataAccess.DataAccessException;
import dataAccess.MemGameAccess;
import dataAccess.MemUserAccess;
import passoffTests.testClasses.TestModels;
import service.DBService;
import service.GameService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public Object handle(Request req, Response res) {
        // clear games
        MemUserAccess clear = new MemUserAccess();
        try {
            clear.clearAllUsers();
        } catch(DataAccessException e) {
            res.status(500);
            // use the serializer gson to do the below
            String body = "";
            res.body(body);

        }

        ;

        // clear users
        // clear auth tokens
        return req.body();
    }
    // method that takes in a spark request and response (spark objects to read from and write to)
    // parse and deserialize (if needed)
    // calls service method
    // serialize return object into the body (return Gson string from serialization and write whatever you need to the response object (status))
    // i assume try catches could also go in here
}

