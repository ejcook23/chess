package dataAccess;

import model.UserData;

public interface UserAccess {

    // READ A SINGLE USER
    UserData getUser(String username) throws DataAccessException;
    // DELETE ALL USERS
    void clearAllUsers() throws DataAccessException;

    // CREATE A SINGLE USER
    String registerUser(String username, String password, String email) throws DataAccessException;


}
