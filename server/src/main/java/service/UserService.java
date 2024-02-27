package service;

import dataAccess.DataAccessException;
import dataAccess.MemUserAccess;
import dataAccess.UserAccess;
import model.AuthData;
import model.UserData;

public class UserService {

    UserAccess userDAO;

    public UserService(UserAccess userDAO) {
        this.userDAO = userDAO;

    }

    public AuthData register(UserData user) throws DataAccessException {
        // calls MemoryUserAccess functionality
        if(userDAO.getUser(user.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        if(user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        String authToken = userDAO.registerUser(user.username(), user.password(), user.email());

        return new AuthData(authToken,user.username());
    }

    public AuthData login(UserData user) {
        return new AuthData("tokenPlaceholder(login)","usernamePlaceholder(login)");
    }

    public void logout(UserData user) {}


}

