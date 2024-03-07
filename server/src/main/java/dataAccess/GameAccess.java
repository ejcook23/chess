package dataAccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public interface GameAccess {

    void createGame(String gameName) throws DataAccessException, SQLException;

    boolean gameExistsByID(int gameID) throws DataAccessException;

    boolean gameExistsByName(String gameName) throws DataAccessException;

    public Integer getGameIDByName(String gameName) throws DataAccessException, SQLException;

    public GameData getGameData(Integer gameID) throws DataAccessException, SQLException;

    public Collection<GameData> getAllGames() throws DataAccessException;

    public void clearGames() throws DataAccessException;

    public boolean blackPlayerFree(int gameID) throws DataAccessException;

    public boolean whitePlayerFree(int gameID) throws DataAccessException;

    public void setBlackUser(int gameID, String username) throws DataAccessException;

    public void setWhiteUser(int gameID, String username) throws DataAccessException;



}


