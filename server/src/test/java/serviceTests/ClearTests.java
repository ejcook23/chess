package serviceTests;

import dataAccess.*;
import model.UserAndAuthResponse;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.sql.SQLException;

public class ClearTests {


    @Test
    public void clearPos() throws DataAccessException, SQLException {
        UserAccess userDAO = new MemUserAccess();
        AuthAccess authDAO = new MemAuthAccess();

        UserService service = new UserService(userDAO, authDAO);

        UserAndAuthResponse response = service.register(new UserData("User","Pass","Email"));

        Assertions.assertEquals("Pass",userDAO.getUserPass("User") );
        Assertions.assertTrue(authDAO.tokenExists(response.authToken()));

        userDAO.clearAllUsers();
        authDAO.clearTokens();

        Assertions.assertNotEquals("Pass",userDAO.getUserPass("User") );
        Assertions.assertFalse(authDAO.tokenExists(response.authToken()));


    }
}
