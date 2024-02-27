package dataAccess;

import model.AuthData;

public interface AuthAccess {
    void clearTokens(); // finish this up

    String createAuth(String username);
}
