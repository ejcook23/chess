package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

public class ClearTests {


    @Test
    public void clearPos() throws DataAccessException {
        UserAccess userDAO = new MemUserAccess();
        AuthAccess authDAO = new MemAuthAccess();

        UserService service = new UserService(userDAO, authDAO);

        service.register



        userDAO.addUser("User","Pass","Email");
        String token = authDAO.createAuth("User");

        Assertions.assertEquals("Pass",userDAO.getUserPass("User") );
        Assertions.assertTrue(authDAO.tokenExists(token));

        userDAO.clearAllUsers();
        authDAO.clearTokens();

        Assertions.assertNotEquals("Pass",userDAO.getUserPass("User") );
        Assertions.assertFalse(authDAO.tokenExists(token));


    }
}
