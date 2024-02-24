package handler;

import spark.Request;
import spark.Response;

public class ClearHandler {

    public Object handleRequest(Request req, Response res) {
        return "String";
    }
    // method that takes in a spark request and response (spark objects to read from and write to)
    // parse and deserialize (if needed)
    // calls service method
    // serialize return object into the body (return Gson string from serialization and write whatever you need to the response object (status))
}
