package service;

import dataAccess.AuthAccess;
import dataAccess.GameAccess;
import dataAccess.UserAccess;

public class DBService {

    UserAccess userDAO;
    AuthAccess authDAO;
    GameAccess gameDAO;

    public DBService(UserAccess userDAO, AuthAccess authDAO, GameAccess gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void clearDB() throws Exception {

        userDAO.clearAllUsers();
        authDAO.clearTokens();
        gameDAO.clearGames();
        // clear games as well

    }


}
