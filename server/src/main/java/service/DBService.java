package service;

import dataAccess.DataAccessException;
import dataAccess.MemUserAccess;

public class DBService {

    // clear method
    public void clearDB() throws Exception {
        MemUserAccess memUserAccess = new MemUserAccess();
        memUserAccess.clearAllUsers();
        memUserAccess.clearTokens();
        // clear games as well

    }


}
