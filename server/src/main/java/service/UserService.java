package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.UserAccess;
import model.LoginRequest;
import model.UserAndAuthResponse;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.Objects;

public class UserService {

    UserAccess userDAO;
    AuthAccess authDAO;

    public UserService(UserAccess userDAO, AuthAccess authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;

    }

    public UserAndAuthResponse register(UserData user) throws DataAccessException {
        // callsDAO functionality
        if(userDAO.getUserPass(user.username()) != null || userDAO.getUserMail(user.username()) != null) {
            System.out.println("Error: already taken");
            throw new DataAccessException("Error: already taken");
        }
        else if(Objects.equals(user.username(), "") || Objects.equals(user.password(), "") || Objects.equals(user.email(), "")) {
            System.out.println("Error: bad request");
            throw new DataAccessException("Error: bad request");
        }

        System.out.println("Creating user and getting auth token...");

        //INSERT USER
        userDAO.addUser(user.username(), user.password(), user.email());

        //CREATE AUTH
        String authToken = authDAO.createAuth(user.username());

        return new UserAndAuthResponse(user.username(), authToken);
    }

    public UserAndAuthResponse login(LoginRequest userData) throws DataAccessException{
        if(Objects.equals(userDAO.getUserPass(userData.username()), userData.password())) {
            System.out.println("User confirmed, creating new auth token.");
            String authToken = authDAO.createAuth(userData.username());
            return new UserAndAuthResponse(userData.username(),authToken);
        }
        else if(userDAO.getUserPass(userData.username()) != null) {
            System.out.println("Error: unauthorized");
            throw new DataAccessException("Error: unauthorized");
        }
        else {
            System.out.println("Error: 500");
            throw new DataAccessException("Error: ");
        }

    }

    public void logout(UserData user) {}


}

