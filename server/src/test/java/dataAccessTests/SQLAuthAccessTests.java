package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.sql.SQLException;

public class SQLAuthAccessTests {
    UserAccess userDAO = new SQLUserAccess();
    AuthAccess authDAO = new SQLAuthAccess();

    @Test
    public void clearTokensPos() throws DataAccessException, SQLException {
        authDAO.clearTokens();
        String token1 = authDAO.createAuth("User1");
        String token2 = authDAO.createAuth("User2");
        authDAO.clearTokens();
        Assertions.assertNull(authDAO.getUserFromToken(token1));
        Assertions.assertNull(authDAO.getUserFromToken(token2));
    }


    @Test
    public void tokenExistsPos() throws DataAccessException, SQLException {
        authDAO.clearTokens();
        String token1 = authDAO.createAuth("User1");
        Assertions.assertTrue(authDAO.tokenExists(token1));
    }

    @Test
    public void tokenExistsNeg() throws DataAccessException {
        authDAO.clearTokens();
        Assertions.assertFalse(authDAO.tokenExists("sdhfjsakdhf"));

    }


    @Test
    public void createAuthPos() throws DataAccessException, SQLException {
        authDAO.clearTokens();
        String token = authDAO.createAuth("TestyUser");
        Assertions.assertNotNull(token);
        Assertions.assertTrue(authDAO.tokenExists(token));
    }

    @Test
    public void createAuthNeg() throws DataAccessException {
        authDAO.clearTokens();
        Assertions.assertThrows(Exception.class, () -> authDAO.createAuth(null));
    }


    @Test
    public void delTokenPos() throws DataAccessException, SQLException {
        authDAO.clearTokens();
        String token = authDAO.createAuth("TestyUser");
        authDAO.delToken(token);
        Assertions.assertFalse(authDAO.tokenExists(token));
    }

    @Test
    public void delTokenNeg() throws DataAccessException, SQLException {
        authDAO.clearTokens();
        String token = authDAO.createAuth("TestyUser");
        String fakeToken = "21342341gasdfasf";
        authDAO.delToken(fakeToken);
        Assertions.assertFalse(authDAO.tokenExists(fakeToken));
        Assertions.assertTrue(authDAO.tokenExists(token));
    }




    @Test
    public void getUserFromTokenPos() throws DataAccessException, SQLException {
        authDAO.clearTokens();
        String token = authDAO.createAuth("TestyUser");
        Assertions.assertEquals("TestyUser", authDAO.getUserFromToken(token));
    }

    @Test
    public void getUserFromTokenNeg() throws DataAccessException {
        authDAO.clearTokens();
        Assertions.assertNull(authDAO.getUserFromToken("234234"));
    }

}
