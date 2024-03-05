package serviceTests;

import dataAccess.*;
import model.CreateGameResponse;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

public class ListGamesTests {
    AuthAccess authDAO = new MemAuthAccess();
    GameAccess gameDAO = new MemGameAccess();
    UserAccess userDAO = new MemUserAccess();

    GameService service = new GameService(authDAO, gameDAO, userDAO);

    @Test
    public void listGamesPos() throws DataAccessException {
        String token = authDAO.createAuth("User");

        // Create two games
        service.createGame(token, "testGame");
        service.createGame(token, "testGame2");

        Assertions.assertTrue(gameDAO.getAllGames().size() > 1);
        Assertions.assertTrue( service.listGames(token).games().size() > 1);

    }

    @Test
    public void listGamesNeg() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> service.listGames("dhsajlfsdf"));
    }

}
