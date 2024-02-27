package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.UserAccess;
import model.AuthData;
import model.UserData;

public class UserService {

    UserAccess userDAO;
    AuthAccess authDAO;

    public UserService(UserAccess userDAO, AuthAccess authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;

    }


    public AuthData register(UserData user) throws DataAccessException {
        // callsDAO functionality
        if(userDAO.getUserPass(user.username()) != null && userDAO.getUserMail(user.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        if(user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("Error: bad request");
        }
        //INSERT USER
        userDAO.addUser(user.username(), user.password(), user.email());
        //CREATE AUTH
        String authToken = authDAO.createAuth(user.username());

        return new AuthData(authToken,user.username());
    }

    public AuthData login(UserData user) {
        return new AuthData("tokenPlaceholder(login)","usernamePlaceholder(login)");
    }

    public void logout(UserData user) {}


}

