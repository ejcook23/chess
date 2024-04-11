package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.sql.SQLException;

public class SQLUserAccessTests {

    UserAccess userDAO = new SqlUserAccess();
    AuthAccess authDAO = new SqlAuthAccess();

    UserService service = new UserService(userDAO, authDAO);


    @Test
    public void getUserPassPos() throws DataAccessException, SQLException {
        userDAO.clearAllUsers();
        userDAO.addUser("TestUser","Password","Email");
        Assertions.assertEquals("Password", userDAO.getUserPass("TestUser"));
    }

    @Test
    public void getUserPassNeg() throws DataAccessException {
        userDAO.clearAllUsers();
        Assertions.assertNull(userDAO.getUserPass("Userjdsfjksladf"));
    }


    @Test
    public void getUserMailPos() throws DataAccessException, SQLException {
        userDAO.clearAllUsers();
        userDAO.addUser("TestUser","Password","Email123");
        Assertions.assertEquals("Email123", userDAO.getUserMail("TestUser"));
    }

    @Test
    public void getUserMailNeg() throws DataAccessException{
        userDAO.clearAllUsers();
        Assertions.assertNull(userDAO.getUserMail("Userjdsfjksladf"));
    }


    @Test
    public void clearAllUsersPos() throws DataAccessException, SQLException {
        userDAO.clearAllUsers();
        userDAO.addUser("TestUser","Password","Email");
        userDAO.addUser("TestUser2","Password2","Email2");
        userDAO.clearAllUsers();
        Assertions.assertNull(userDAO.getUserPass("TestUser"));
        Assertions.assertNull(userDAO.getUserPass("TestUser2"));
    }


    @Test
    public void addUserPos() throws DataAccessException, SQLException {
        userDAO.clearAllUsers();
        userDAO.addUser("TestUser","Password","Email");
        Assertions.assertNotNull(userDAO.getUserPass("TestUser"));

    }

    @Test
    public void addUserNeg() throws Exception {
        userDAO.clearAllUsers();
        Assertions.assertThrows(Exception.class, () -> userDAO.addUser("User",null,null));

    }


}
