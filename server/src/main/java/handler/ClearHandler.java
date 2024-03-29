package handler;

import dataAccess.DataAccessException;
import dataAccess.MemGameAccess;
import dataAccess.MemUserAccess;
import model.ErrorMsg;
import passoffTests.testClasses.TestModels;
import service.DBService;
import service.GameService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class ClearHandler {

    private final DBService dbService;
    public ClearHandler(DBService dbService) {
        this.dbService = dbService;
    }
    public Object handle(Request req, Response res) {
        try {
            dbService.clearDB();
            res.body("{}");
            res.status(200);
        } catch(Exception e) {
            Gson gson = new Gson();
            res.body(gson.toJson(new ErrorMsg(e.getMessage())));
            res.status(500);
        }
        return res.body();
    }
    // method that takes in a spark request and response (spark objects to read from and write to)
    // parse and deserialize (if needed)
    // calls service method
    // serialize return object into the body (return Gson string from serialization and write whatever you need to the response object (status))
    // i assume try catches could also go in here
}

