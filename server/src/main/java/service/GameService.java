package service;

import dataAccess.AuthAccess;
import dataAccess.GameAccess;

public class GameService {
    AuthAccess authDAO;
    GameAccess gameDAO;

    public GameService(AuthAccess authDAO, GameAccess gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;

    }

}

