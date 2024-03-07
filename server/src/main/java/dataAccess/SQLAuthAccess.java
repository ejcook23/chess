package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthAccess implements AuthAccess{
    @Override
    public void clearTokens() throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE AuthData")) {
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
        }
    }

    @Override
    public boolean tokenExists(String token) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM AuthData WHERE authToken=?")) {
                preparedStatement.setString(1, token);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var username = rs.getString("username");
                        System.out.printf("[SELECT IF TOKEN EXISTS] Token Exists for Username: %s", username);
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
    public String createAuth(String username) throws SQLException, DataAccessException {
        System.out.println("\nMaking new auth token..");
        String newAuthToken = UUID.randomUUID().toString();

        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO AuthData (authToken , username) VALUES(?, ?)")) {
                preparedStatement.setString(1, newAuthToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();

            }
        } catch (Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw e;
        }

        System.out.println("\ninserted " + newAuthToken);
        return newAuthToken;
    }

    @Override
    public void delToken(String token) throws SQLException, DataAccessException {
        System.out.println("\nDeleting auth token..");

        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM AuthData WHERE authToken=?")) {
                preparedStatement.setString(1, token);
                preparedStatement.executeUpdate();

            }
        } catch (Exception e) {
            System.out.println("SQL Access Error: " + e.getMessage());
            throw e;
        }

        System.out.println("\ndeleted authtoken" + token);
    }

    @Override
    public String getUserFromToken(String token) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM AuthData WHERE authToken=?")) {
                preparedStatement.setString(1, token);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var username = rs.getString("username");
                        System.out.printf("[SELECT IF TOKEN EXISTS] Username: %s", username);
                        return username;
                    }
                }
            }
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
        }
        return null;
    }
}
