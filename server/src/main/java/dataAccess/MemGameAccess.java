package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemGameAccess implements GameAccess{
    Map<Integer, GameData> gameList = new HashMap<>();
    Map<String, Integer> gameNameID = new HashMap<String, Integer>();
    ArrayList<Integer> gameIDs = new ArrayList<Integer>();

    @Override
    public void createGame(String gameName) {
        int gameID = giveValidGameID();
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        gameList.put(gameID, newGame);
        gameNameID.put(gameName, gameID);
        gameIDs.add(gameID);
    }

    @Override
    public boolean blackPlayerFree(int gameID) {
        return (gameList.get(gameID).blackUsername() == null);
    }
    @Override
    public boolean whitePlayerFree(int gameID) {
        return (gameList.get(gameID).whiteUsername() == null);
    }
    @Override
    public void setBlackUser(int gameID, String username) {
        GameData newGame = new GameData(gameID, gameList.get(gameID).whiteUsername(), username,gameList.get(gameID).gameName(),gameList.get(gameID).game());
        gameList.remove(gameID);
        gameList.put(gameID, newGame);
    }
    @Override
    public void setWhiteUser(int gameID, String username) {
        GameData newGame = new GameData(gameID, username, gameList.get(gameID).blackUsername(),gameList.get(gameID).gameName(),gameList.get(gameID).game());
        gameList.remove(gameID);
        gameList.put(gameID, newGame);
    }
    @Override
    public boolean gameExistsByName(String gameName) {
        return gameNameID.get(gameName) != null;
    }

    @Override
    public boolean gameExistsByID(int gameID) {
       return gameList.get(gameID) != null;
    }
    @Override
    public GameData getGameData(Integer gameID) {
        return gameList.get(gameID);
    }
    @Override
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
    @Override
    public Collection<GameData> getAllGames() {
        return gameList.values();
    }
    @Override
    public void clearGames() {
        gameList.clear();
        gameIDs.clear();
        gameNameID.clear();
    }

}
