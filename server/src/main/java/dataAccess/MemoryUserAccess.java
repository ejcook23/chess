package dataAccess;

import model.UserData;
import java.util.*;

public class MemoryUserAccess implements UserAccess {
    Map<String, String> userPass = new HashMap<String, String>();
    Map<String, String> userMail = new HashMap<String, String>();


    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearAllUsers() {
        userPass.clear();
        userMail.clear();
    }


}
