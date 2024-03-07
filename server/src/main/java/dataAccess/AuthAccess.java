package dataAccess;

import model.AuthData;

public interface AuthAccess {
    void clearTokens() throws DataAccessException; // finish this up

    boolean tokenExists(String token);

    String createAuth(String username);

    void delToken(String token);

    String getUserFromToken(String token);
}
