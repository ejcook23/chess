package dataAccess;

import model.UserData;

import java.sql.SQLException;

public interface UserAccess {

    String getUserPass(String username) throws DataAccessException;

    String getUserMail(String username) throws DataAccessException;

    // DELETE ALL USERS
    void clearAllUsers() throws DataAccessException;

    // CREATE A SINGLE USER
    void addUser(String username, String password, String email) throws DataAccessException, SQLException;



}
