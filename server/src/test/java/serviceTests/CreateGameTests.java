package serviceTests;

import dataAccess.*;
import model.CreateGameResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.sql.SQLException;


public class CreateGameTests {
    AuthAccess authDAO = new MemAuthAccess();
    GameAccess gameDAO = new MemGameAccess();
    UserAccess userDAO = new MemUserAccess();

    GameService service = new GameService(authDAO, gameDAO, userDAO);

    @Test
    public void createGamePos() throws DataAccessException, SQLException {
        String token = authDAO.createAuth("User");
        CreateGameResponse response = service.createGame(token, "testGame");

        Assertions.assertTrue(gameDAO.gameExistsByID(response.gameID()));
        Assertions.assertTrue(gameDAO.gameExistsByName("testGame"));
        Assertions.assertNotNull(gameDAO.getGameData(response.gameID()));
        Assertions.assertEquals(gameDAO.getGameData(response.gameID()).gameName(),"testGame");
        Assertions.assertEquals(gameDAO.getGameData(response.gameID()).gameID(),response.gameID());

    }

    @Test
    public void createGameNeg() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> service.createGame("whee","game"));
    }

}
