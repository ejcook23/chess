package service;

import dataAccess.DataAccessException;
import dataAccess.MemUserAccess;
import dataAccess.UserAccess;

public class DBService {

    UserAccess userDAO;

    public DBService(UserAccess userDAO) {
        this.userDAO = userDAO;
    }
    // clear method
    public void clearDB() throws Exception {

        userDAO.clearAllUsers();
        userDAO.clearTokens();
        // clear games as well

    }


}
