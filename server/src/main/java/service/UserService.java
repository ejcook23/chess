package service;

import dataAccess.AuthAccess;
import dataAccess.DataAccessException;
import dataAccess.UserAccess;
import model.LoginRequest;
import model.UserAndAuthResponse;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.crypto.Data;
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
        else if(Objects.equals(user.username(), null) || Objects.equals(user.password(), null) || Objects.equals(user.email(), null)) {
            System.out.println("Error: bad request");
            throw new DataAccessException("Error: bad request");
        }

        System.out.println("Creating user and getting auth token...");

        // ENCRYPT PASSWORD
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(user.password());

        //INSERT USER
        userDAO.addUser(user.username(), hashedPassword, user.email());

        //CREATE AUTH
        String authToken = authDAO.createAuth(user.username());

        return new UserAndAuthResponse(user.username(), authToken);
    }

    public UserAndAuthResponse login(LoginRequest userData) throws DataAccessException{
        //HASHES user provided password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userData.password());

        // compares hashes of passwords
        if(Objects.equals(userDAO.getUserPass(userData.username()), hashedPassword)) {
            System.out.println("User confirmed, creating new auth token.");
            String authToken = authDAO.createAuth(userData.username());
            return new UserAndAuthResponse(userData.username(),authToken);
        }
        // if there is no such user, or the username and password do not match
        else if(userDAO.getUserPass(userData.username()) != null || !(Objects.equals(userDAO.getUserPass(userData.username()), userData.password()))) {
            System.out.println("Error: unauthorized");
            throw new DataAccessException("Error: unauthorized");
        }
        else {
            System.out.println("Error: 500");
            throw new DataAccessException("Error: ");
        }

    }

    public void logout(String token) throws DataAccessException {
        if(authDAO.tokenExists(token)) {
            System.out.println("Logging user out (removing auth token)");
            authDAO.delToken(token);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }

    }

}

