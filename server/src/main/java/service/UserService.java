package service;

import dataAccess.DataAccessException;
import dataAccess.MemUserAccess;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    public AuthData register(UserData user) throws DataAccessException {
        // calls MemoryUserAccess functionality
        MemUserAccess memUserAccess = new MemUserAccess();
        String authToken = memUserAccess.registerUser(user.username(), user.password(), user.email());
        return new AuthData(authToken,user.username());
    }

    public AuthData login(UserData user) {
        return new AuthData("tokenPlaceholder(login)","usernamePlaceholder(login)");
    }

    public void logout(UserData user) {}


}

