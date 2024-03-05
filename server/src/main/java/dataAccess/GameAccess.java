package dataAccess;

import chess.ChessGame;
import model.GameData;

public interface GameAccess {

    void createGame(String gameName);

    boolean gameExistsByID(int gameID);

    boolean gameExistsByName(String gameName);

    public int getGameIDByName(String gameName);

    public GameData getGameData(Integer gameID);

}


