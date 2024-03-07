package dataAccess;

import model.AuthData;

import java.sql.SQLException;

public interface AuthAccess {
    void clearTokens() throws DataAccessException; // finish this up

    boolean tokenExists(String token) throws DataAccessException;

    String createAuth(String username) throws SQLException, DataAccessException;

    void delToken(String token) throws SQLException, DataAccessException;

    String getUserFromToken(String token) throws DataAccessException;
}
