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



    @Override
    // returns a newly generated token for that user
    public String registerUser(String username, String password, String email) throws DataAccessException {

        userPass.put(username,password);
        userMail.put(username,email);
        String newAuth = genAuthToken();
        authTokens.put(username,newAuth);

        System.out.println(userPass);
        System.out.println(authTokens);

        return newAuth;

    }


    public String genAuthToken() {
        return UUID.randomUUID().toString();
    }


}
