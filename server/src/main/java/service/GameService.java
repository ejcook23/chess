package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.GameAccess;
import dataAccess.UserAccess;
import model.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    AuthAccess authDAO;
    GameAccess gameDAO;
    UserAccess userDAO;

    public GameService(AuthAccess authDAO, GameAccess gameDAO, UserAccess userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;

    }

    public CreateGameResponse createGame(String header, String gameName) throws DataAccessException, SQLException {
        if(authDAO.tokenExists(header)) {
            if(!gameDAO.gameExistsByName(gameName)) {
                System.out.println("Creating new game...");
                gameDAO.createGame(gameName);
                return new CreateGameResponse(gameDAO.getGameIDByName(gameName));
            }
            else {
                throw new DataAccessException("Error: game name already exists");
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

    public void joinGame(JoinGameRequest request, String header) throws DataAccessException {
        String playerColor = request.playerColor();
        int gameID = request.gameID();

        if(!authDAO.tokenExists(header)) {
            throw new DataAccessException("Error: unauthorized");
        }

        if(gameDAO.gameExistsByID(gameID)) {
            // IF THE PLAYER IS REQUESTING BLACK AND IT IS FREE..
            if(Objects.equals(playerColor, "BLACK")) {
                if(gameDAO.blackPlayerFree(gameID)) {
                    gameDAO.setBlackUser(gameID, authDAO.getUserFromToken(header));
                } else {
                    throw new DataAccessException("Error: already taken");
                }
                // IF THE PLAYER IS REQUESTING WHITE AND IT IS FREE...
            }
            else if (Objects.equals(playerColor, "WHITE")) {
                if(gameDAO.whitePlayerFree(gameID)) {
                    gameDAO.setWhiteUser(gameID, authDAO.getUserFromToken(header));
                } else {
                    throw new DataAccessException("Error: already taken");
                }
            }
            else if (playerColor != null) {
                throw new DataAccessException("Error: bad request");
            }
        // GAME DOES NOT EXIST
        } else {
            throw new DataAccessException("Error: bad request");
        }




    }


}

