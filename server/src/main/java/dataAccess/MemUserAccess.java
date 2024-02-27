package dataAccess;

import model.UserData;

import java.util.*;

public class MemUserAccess implements UserAccess {
    Map<String, String> userPass = new HashMap<String, String>();
    Map<String, String> userMail = new HashMap<String, String>();
    Map<String, String> authTokens = new HashMap<String, String>();


    @Override
    public UserData getUser(String username) throws DataAccessException {
        if(userPass.get(username) == null) {
            throw new DataAccessException("Attempted to GET a nonexistent user.");
        }
        else {
            return new UserData(username,userPass.get(username),userMail.get(username));
        }
    }

    @Override
    public void clearAllUsers() throws DataAccessException {
        userPass.clear();
        userMail.clear();
    }

    public void clearTokens() throws DataAccessException {
        authTokens.clear();
    }

    @Override
    /**
      @return returns a newly generated auth token for the user
     */
    public String registerUser(String username, String password, String email) throws DataAccessException {
        if(userPass.get(username) != null) {
            throw new DataAccessException("Error: already taken");
        }
        if(username == null || password == null || email == null) {
            throw new DataAccessException("Error: bad request");
        }
        else {
            userPass.put(username,password);
            userMail.put(username,email);
            authTokens.put(username,genAuthToken());
        }

        return username;
    }


    public String genAuthToken() {
        return UUID.randomUUID().toString();
    }


}
