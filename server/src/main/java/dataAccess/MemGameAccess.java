package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemGameAccess implements GameAccess{
    Map<Integer, GameData> gameList = new HashMap<>();
    Map<String, Integer> gameNameID = new HashMap<String, Integer>();
    ArrayList<Integer> gameIDs = new ArrayList<Integer>();

    @Override
    public void createGame(String gameName) {
        int gameID = giveValidGameID();
        GameData newGame = new GameData(gameID, "", "", gameName, new ChessGame());
        gameList.put(gameID, newGame);
        gameNameID.put(gameName, gameID);
        gameIDs.add(gameID);
    }

    public boolean gameExistsByName(String gameName) {
        return gameNameID.get(gameName) != null;
    }


    public boolean gameExistsByID(int gameID) {
       return gameList.get(gameID) != null;
    }

    public GameData getGameData(Integer gameID) {
        return gameList.get(gameID);
    }

    public int getGameIDByName(String gameName) {
        return gameNameID.get(gameName);
    }

    public int giveValidGameID() {
        int gameID = 0;
        for(int i = 100; i < 999;) {
            if(gameIDs.contains(i)) {
                i++;
            }
            else {
                gameID = i;
                break;
            }
        }
        gameIDs.add(gameID);
        return gameID;
    }
}
