package dataAccess;

import model.UserData;

public interface UserAccess {
    UserData getUser(String username) throws DataAccessException;

    // verify user


    // clear all

}
