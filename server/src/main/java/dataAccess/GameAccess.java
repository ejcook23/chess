package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public interface GameAccess {

    void createGame(String gameName) throws DataAccessException, SQLException;

    boolean gameExistsByID(int gameID) throws DataAccessException;

    boolean gameExistsByName(String gameName) throws DataAccessException;

    public int getGameIDByName(String gameName) throws DataAccessException, SQLException;

    public GameData getGameData(Integer gameID) throws DataAccessException;

    public Collection<GameData> getAllGames();

    public void clearGames();

    public boolean blackPlayerFree(int gameID);

    public boolean whitePlayerFree(int gameID);

    public void setBlackUser(int gameID, String username);

    public void setWhiteUser(int gameID, String username);



}


