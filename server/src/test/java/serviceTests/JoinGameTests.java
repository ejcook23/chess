package serviceTests;

import dataAccess.*;
import model.CreateGameResponse;
import model.JoinGameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.sql.SQLException;

public class JoinGameTests {
    AuthAccess authDAO = new MemAuthAccess();
    GameAccess gameDAO = new MemGameAccess();
    UserAccess userDAO = new MemUserAccess();

    GameService service = new GameService(authDAO, gameDAO, userDAO);

    @Test
    public void joinGamePos() throws DataAccessException, SQLException {
        String token = authDAO.createAuth("User");
        CreateGameResponse response = service.createGame(token, "testGame");
        JoinGameRequest request = new JoinGameRequest("BLACK",response.gameID());
        service.joinGame(request, token);

        Assertions.assertFalse(gameDAO.blackPlayerFree(response.gameID()));
    }

    @Test
    public void joinGameNeg() throws DataAccessException, SQLException {
        String token = authDAO.createAuth("User");
        String token2 = authDAO.createAuth("User2");
        CreateGameResponse response = service.createGame(token, "testGame");
        JoinGameRequest request = new JoinGameRequest("BLACK",response.gameID());
        JoinGameRequest request2 = new JoinGameRequest("BLACK",response.gameID());
        service.joinGame(request, token);

        // Try to join a game where the black user is already taken
        Assertions.assertThrows(DataAccessException.class, () -> service.joinGame(request2, token2));

    }

}
