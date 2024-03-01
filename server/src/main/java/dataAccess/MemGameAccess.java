package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemGameAccess implements GameAccess{
    Map<Integer, GameData> gameList = new HashMap<>();

    @Override
    public void addGame(int gameID, String whiteUser, String blackUser, String gameName, ChessGame game) {
        GameData newGame = new GameData(gameID, whiteUser, blackUser, gameName, game);
        gameList.put(gameID, newGame);
    }
}
