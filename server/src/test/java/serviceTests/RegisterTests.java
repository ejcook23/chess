package serviceTests;

import dataAccess.*;
import model.UserAndAuthResponse;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.sql.SQLException;

public class RegisterTests {

    UserAccess userDAO = new MemUserAccess();
    AuthAccess authDAO = new MemAuthAccess();

    UserService service = new UserService(userDAO, authDAO);

    @Test
    public void registerPos() throws DataAccessException, SQLException {

        // REGISTER USER
        UserAndAuthResponse response = service.register(new UserData("User","Pass","Email"));

        // CHECK THAT PASS AND EMAIL WERE IN CORRECT FOR USER, and that AUTH TOKEN EXISTS
        Assertions.assertEquals("Email",userDAO.getUserMail("User"));
        Assertions.assertTrue(authDAO.tokenExists(response.authToken()));

        //CLEAR

    }

    @Test
    public void registerNeg() {

        // REGISTER NEGATIVE
        Assertions.assertThrows(DataAccessException.class,() -> service.register(new UserData("User1",null,"Email")));

    }
}
