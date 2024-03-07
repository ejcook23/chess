package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;

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
    public boolean tokenExists(String token) {
        return false;
    }

    @Override
    public String createAuth(String username) {
        return null;
    }

    @Override
    public void delToken(String token) {

    }

    @Override
    public String getUserFromToken(String token) {
        return null;
    }
}
