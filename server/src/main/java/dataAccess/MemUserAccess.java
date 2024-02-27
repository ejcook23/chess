package dataAccess;

import java.util.*;

public class MemUserAccess implements UserAccess {
    Map<String, String> userPass = new HashMap<>();
    Map<String, String> userMail = new HashMap<>();


    @Override
    public String getUserPass(String username) {
        return userPass.get(username);
    }

    @Override
    public String getUserMail(String username) {
        return userMail.get(username);
    }


    @Override
    public void clearAllUsers() {
        userPass.clear();
        userMail.clear();
    }

    @Override
    public void addUser(String username, String password, String email) {
        userPass.put(username,password);
        userMail.put(username,email);
        System.out.println(userPass);
    }



}
