package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.GameAccess;
import model.CreateGameResponse;
import model.GameData;
import model.ListGamesResponse;

import java.util.Collection;
import java.util.List;

public class GameService {
    AuthAccess authDAO;
    GameAccess gameDAO;

    public GameService(AuthAccess authDAO, GameAccess gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;

    }

    public CreateGameResponse createGame(String header, String gameName) throws DataAccessException {
        if(authDAO.tokenExists(header)) {
            if(!gameDAO.gameExistsByName(gameName)) {
                System.out.println("Creating new game...");
                gameDAO.createGame(gameName);
                return new CreateGameResponse(gameDAO.getGameIDByName(gameName));
            }
            else {
                throw new DataAccessException("Error: bad request");
            }
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public ListGamesResponse listGames(String header) throws DataAccessException {
        if(authDAO.tokenExists(header)) {
            return new ListGamesResponse(gameDAO.getAllGames());
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

}

