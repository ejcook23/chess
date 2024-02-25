package dataAccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import javax.xml.crypto.Data;
import java.util.*;

public class MemUserAccess implements UserAccess {
    Map<String, String> userPass = new HashMap<String, String>();
    Map<String, String> userMail = new HashMap<String, String>();


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
    public void clearAllUsers() {
        userPass.clear();
        userMail.clear();
    }

    @Override
    public UserData registerUser(String username, String password, String email) throws DataAccessException {
        if(userPass.get(username) != null) {
            throw new DataAccessException("Attempted to CREATE an already existing user.");
        }
        else {
            userPass.put(username,password);
            userMail.put(username,email);
            return (new UserData(username,password,email));
        }

    }


}
