package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.GameAccess;
import model.CreateGameResponse;
import model.GameData;
import model.GameDataNoGame;
import model.ListGamesResponse;

import java.util.Collection;
import java.util.ArrayList;

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
        Collection<GameDataNoGame> games = new ArrayList<>();
        if(authDAO.tokenExists(header)) {
            for(GameData game : gameDAO.getAllGames()) {
                GameDataNoGame gameDataNoGame = new GameDataNoGame(game.gameID(),game.whiteUsername(),game.blackUsername(),game.gameName());
                games.add(gameDataNoGame);
            }
            return new ListGamesResponse(games);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

}

