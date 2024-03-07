package serviceTests;

import dataAccess.*;
import model.LoginRequest;
import model.UserAndAuthResponse;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class LoginTests {

    UserAccess userDAO = new MemUserAccess();
    AuthAccess authDAO = new MemAuthAccess();

    UserService service = new UserService(userDAO, authDAO);

    @Test
    public void loginPos() throws DataAccessException, SQLException {

        service.register(new UserData("User","Pass","Email"));
        UserAndAuthResponse response = service.login(new LoginRequest("User","Pass"));
        Assertions.assertTrue(authDAO.tokenExists(response.authToken()));
    }

    @Test
    public void loginNeg() throws DataAccessException {

        Assertions.assertThrows(DataAccessException.class, () -> service.login(new LoginRequest("User","beepboop")));
    }
}
