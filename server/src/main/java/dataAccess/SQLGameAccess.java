package dataAccess;

import model.GameData;

import java.util.Collection;

public class SQLGameAccess implements GameAccess{
    @Override
    public void createGame(String gameName) {

    }

    @Override
    public boolean gameExistsByID(int gameID) {
        return false;
    }

    @Override
    public boolean gameExistsByName(String gameName) {
        return false;
    }

    @Override
    public int getGameIDByName(String gameName) {
        return 0;
    }

    @Override
    public GameData getGameData(Integer gameID) {
        return null;
    }

    @Override
    public Collection<GameData> getAllGames() {
        return null;
    }

    @Override
    public void clearGames() {

    }

    @Override
    public boolean blackPlayerFree(int gameID) {
        return false;
    }

    @Override
    public boolean whitePlayerFree(int gameID) {
        return false;
    }

    @Override
    public void setBlackUser(int gameID, String username) {

    }

    @Override
    public void setWhiteUser(int gameID, String username) {

    }
}
