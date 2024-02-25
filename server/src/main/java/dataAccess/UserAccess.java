package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserAccess {

    // READ A SINGLE USER
    UserData getUser(String username) throws DataAccessException;
    // DELETE ALL USERS
    void clearAllUsers();

    // CREATE A SINGLE USER
    UserData registerUser(String username, String password, String email) throws DataAccessException;


}
