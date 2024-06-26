package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameAccess implements GameAccess{
    @Override
    public void createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        var gameJson = new Gson().toJson(game);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO GameData (whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?)")) {
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, gameName);
                preparedStatement.setString(4, gameJson);

                preparedStatement.executeUpdate();

            } catch (Exception e) {
                System.out.println("\n[CREATE GAME] SQL Access Error: " + e.getMessage());
                throw new DataAccessException(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("\nError getting connection.\n");
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public boolean gameExistsByID(int gameID) throws DataAccessException {
            try(Connection conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement("SELECT game FROM GameData WHERE gameID=?")) {
                    preparedStatement.setString(1, Integer.toString(gameID));
                    try (var rs = preparedStatement.executeQuery()) {
                        if (rs.next()) {
                            System.out.printf("\n[GAME EXISTS BY ID] Game exists by ID: %s", gameID);
                            return true;
                        }
                    }
                }
            } catch(Exception e) {
                System.out.println("SQL Access Error: " + e.getMessage());
                throw new DataAccessException(e.getMessage());
            }
            return false;
    }

    @Override
    public boolean gameExistsByName(String gameName) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT game FROM GameData WHERE gameName=?")) {
                preparedStatement.setString(1, gameName);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        System.out.printf("\n[GAME EXISTS BY NAME] GAME Exists for GameName: %s", gameName);
                        return true;
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
        return false;
    }

    @Override
    public Integer getGameIDByName(String gameName) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT gameID FROM GameData WHERE gameName=?")) {
                preparedStatement.setString(1, gameName);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var gameID = rs.getInt("gameID");
                        System.out.printf("\n[GET GAME ID BY NAME] Game by Name Exists, ID: %s", gameID);
                        return gameID;
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public GameData getGameData(Integer gameID) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM GameData WHERE gameID=?")) {
                preparedStatement.setString(1, Integer.toString(gameID));
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var rsGameID = rs.getString("gameID");
                        var whiteUser = rs.getString("whiteUsername");
                        var blackUser = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var gameJson = rs.getString("game");

                        System.out.printf("\n[GET GAME DATA] JsonString: " + gameJson);
                        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
                        int gameIDint = Integer.parseInt(rsGameID);

                        return new GameData(gameIDint,whiteUser,blackUser,gameName,game);
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<GameData> getAllGames() throws DataAccessException {
        Collection<GameData> gameList = new ArrayList<>();
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM GameData")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var gameID = rs.getString("gameID");
                        var whiteUser = rs.getString("whiteUsername");
                        var blackUser = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var gameJson = rs.getString("game");

                        System.out.printf("\n[GET ALL GAMES] JsonString: " + gameJson);
                        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
                        int gameIDint = Integer.parseInt(gameID);

                        GameData gameInfo = new GameData(gameIDint,whiteUser,blackUser,gameName,game);

                        gameList.add(gameInfo);
                    }
                }
            }
            return gameList;
        } catch(Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearGames() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE GameData")) {
                preparedStatement.executeUpdate();
            }
        } catch(Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean blackPlayerFree(int gameID) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT blackUsername FROM GameData WHERE gameID=?")) {
                preparedStatement.setString(1, Integer.toString(gameID));
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var blackUsername = rs.getString("blackUsername");
                        System.out.printf("\n[BLACK PLAYER FREE] USERNAME: %s", blackUsername);
                        return blackUsername == null;
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean whitePlayerFree(int gameID) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT whiteUsername FROM GameData WHERE gameID=?")) {
                preparedStatement.setString(1, Integer.toString(gameID));
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var whiteUsername = rs.getString("whiteUsername");
                        System.out.printf("\n[BLACK PLAYER FREE] USERNAME: %s", whiteUsername);
                        return whiteUsername == null;
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
        return true;
    }

    @Override
    public void setBlackUser(int gameID, String username) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE GameData SET blackUsername=? WHERE gameID=?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, Integer.toString(gameID));

                preparedStatement.executeUpdate();
            }
        } catch(Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(int gameID, String chessGame) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE GameData SET game=? WHERE gameID=?")) {
                preparedStatement.setString(1, chessGame);
                preparedStatement.setString(2, Integer.toString(gameID));
                preparedStatement.executeUpdate();
            }
        } catch(Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGameID(int currGameID, int newNum) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE GameData SET gameID=? WHERE gameID=?")) {
                preparedStatement.setString(1, Integer.toString(newNum));
                preparedStatement.setString(2, Integer.toString(currGameID));
                preparedStatement.executeUpdate();
            }
        } catch(Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void setWhiteUser(int gameID, String username) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE GameData SET whiteUsername=? WHERE gameID=?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, Integer.toString(gameID));

                preparedStatement.executeUpdate();
            }
        } catch(Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

}
