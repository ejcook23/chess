package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameAccess {

    void createGame(String gameName);

    boolean gameExistsByID(int gameID);

    boolean gameExistsByName(String gameName);

    public int getGameIDByName(String gameName);

    public GameData getGameData(Integer gameID);

    public Collection<GameData> getAllGames();

    public void clearGames();

    public boolean blackPlayerFree(int gameID);

    public boolean whitePlayerFree(int gameID);

    public void setBlackUser(int gameID, String username);

    public void setWhiteUser(int gameID, String username);



}


