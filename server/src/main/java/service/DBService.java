package service;

import dataAccess.AuthAccess;
import dataAccess.UserAccess;

public class DBService {

    UserAccess userDAO;
    AuthAccess authDAO;

    public DBService(UserAccess userDAO, AuthAccess authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public void clearDB() throws Exception {

        userDAO.clearAllUsers();
        authDAO.clearTokens();
        // clear games as well

    }


}
