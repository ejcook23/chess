package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.UserAccess;
import model.AuthData;
import model.RegisterResponse;
import model.UserData;

import java.util.Objects;

public class UserService {

    UserAccess userDAO;
    AuthAccess authDAO;

    public UserService(UserAccess userDAO, AuthAccess authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;

    }


    public RegisterResponse register(UserData user) throws DataAccessException {
        // callsDAO functionality
        if(userDAO.getUserPass(user.username()) != null || userDAO.getUserMail(user.username()) != null) {
            System.out.println("Error: already taken");
            throw new DataAccessException("Error: already taken");
        }
        if(Objects.equals(user.username(), "") || Objects.equals(user.password(), "") || Objects.equals(user.email(), "")) {
            System.out.println("Error: bad request");
            throw new DataAccessException("Error: bad request");
        }
        //INSERT USER
        System.out.println("Creating user and getting auth token...");
        userDAO.addUser(user.username(), user.password(), user.email());
        //CREATE AUTH
        String authToken = authDAO.createAuth(user.username());

        return new RegisterResponse(user.username(), authToken);
    }

    public AuthData login(UserData user) {
        return new AuthData("tokenPlaceholder(login)","usernamePlaceholder(login)");
    }

    public void logout(UserData user) {}


}

