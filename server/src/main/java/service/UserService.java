package service;

import model.AuthData;
import model.UserData;

public class UserService {

    public AuthData register(UserData user) {
        // calls dataAccess functionality
        return new AuthData("tokenPlaceholder(register)","usernamePlaceholder(register)");
    }

    public AuthData login(UserData user) {
        return new AuthData("tokenPlaceholder(login)","usernamePlaceholder(login)");
    }

    public void logout(UserData user) {}

}

