package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameAccess implements GameAccess{
    @Override
    public void createGame(String gameName) throws DataAccessException, SQLException {
        ChessGame game = new ChessGame();
        var gameJson = new Gson().toJson(game);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO GameData (whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?)")) {
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, gameName);
                preparedStatement.setString(3, gameJson);

                preparedStatement.executeUpdate();

            } catch (Exception e) {
                System.out.println("\n[CREATE GAME] SQL Access Error: " + e.getMessage());
                throw e;
            }
        }
    }


    @Override
    public boolean gameExistsByID(int gameID) throws DataAccessException {
            try(Connection conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement("SELECT game FROM GameData WHERE gameID=?")) {
                    preparedStatement.setString(1, Integer.toString(gameID));
                    try (var rs = preparedStatement.executeQuery()) {
                        if (rs.next()) {
                            var gameIDresult = rs.getString("gameID");
                            System.out.printf("\n[GAME EXISTS BY ID] Game exists by ID: %s", gameIDresult);
                            return true;
                        }
                    }
                }
            } catch(SQLException e) {
                System.out.println("SQL Access Error: " + e.getMessage());
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
                        var username = rs.getString("username");
                        System.out.printf("\n[GAME EXISTS BY NAME] GAME Exists for GameName: %s", gameName);
                        return true;
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int getGameIDByName(String gameName) throws DataAccessException, SQLException {
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
            throw e;
        }
        return 0;
    }

    @Override
    public GameData getGameData(Integer gameID) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT game FROM GameData WHERE gameID=?")) {
                preparedStatement.setString(1, Integer.toString(gameID));
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var gameJson = rs.getString("game");
                        System.out.printf("\n[GET GAME DATA] JsonString: " + gameJson);
                        return new Gson().fromJson(gameJson, GameData.class);
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<GameData> getAllGames() throws DataAccessException {
        Collection<GameData> gameList = new ArrayList<>();
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT game FROM GameData")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var gameJson = rs.getString("game");
                        System.out.printf("\n[GET ALL GAMES] JsonString: " + gameJson);
                        GameData game = new Gson().fromJson(gameJson, GameData.class);
                        gameList.add(game);
                    }
                }
            }
            return gameList;
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void clearGames() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE GameData")) {
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
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
                        return false;
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
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
                        return false;
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
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
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
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
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
        }
    }

}
