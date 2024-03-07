package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.GameAccess;
import dataAccess.SQLGameAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SQLGameAccessTests {
    GameAccess gameDAO = new SQLGameAccess();

    @Test
    public void createGamePos() throws DataAccessException, SQLException {
        gameDAO.clearGames();
        gameDAO.createGame("TestGame");
        Assertions.assertNotNull(gameDAO.getAllGames());
    }

    @Test // NEED TO FIX THIS TEST.. not sure why it's not throwing.
    public void createGameNeg() throws DataAccessException {
        gameDAO.clearGames();
        Assertions.assertThrows(Exception.class, () -> gameDAO.createGame(null));
        Assertions.assertEquals(0, gameDAO.getAllGames().size()); //need to assert instead that the array is empty
    }


    @Test
    public void gameExistsByIDPos() throws SQLException, DataAccessException {
        gameDAO.clearGames();
        gameDAO.createGame("TestGame");
        Integer id = gameDAO.getGameIDByName("TestGame");
        Assertions.assertTrue(gameDAO.gameExistsByID(id));

    }

    @Test
    public void gameExistsByIDNeg() throws DataAccessException {
        gameDAO.clearGames();
        Assertions.assertFalse(gameDAO.gameExistsByID(1234));

    }


    @Test
    public void gameExistsByNamePos() throws DataAccessException, SQLException {
        gameDAO.clearGames();
        Assertions.assertFalse(gameDAO.gameExistsByName("TestGame"));
        gameDAO.createGame("TestGame");
        Assertions.assertTrue(gameDAO.gameExistsByName("TestGame"));
    }

    @Test
    public void gameExistsByNameNeg() throws DataAccessException {
        gameDAO.clearGames();
        Assertions.assertFalse(gameDAO.gameExistsByName("TestGame"));

    }


    @Test
    public void gameIDByNamePos() throws DataAccessException, SQLException {
        gameDAO.clearGames();
        gameDAO.createGame("TestGame");
        Assertions.assertNotNull(gameDAO.getGameIDByName("TestGame"));
    }

    @Test
    public void gameIDByNameNeg() throws DataAccessException, SQLException {
        gameDAO.clearGames();
        Assertions.assertNull(gameDAO.getGameIDByName("TestGame"));
    }


    @Test
    public void getGameDataPos() throws DataAccessException, SQLException {
        gameDAO.clearGames();
        gameDAO.createGame("TestGame");
        int id = gameDAO.getGameIDByName("TestGame");
        Assertions.assertNotNull(gameDAO.getGameData(id));


    }

    @Test
    public void getGameDataNeg() throws DataAccessException, SQLException {
        gameDAO.clearGames();
        Assertions.assertNull(gameDAO.getGameData(1234));

    }


    @Test
    public void getAllGamesPos() throws DataAccessException, SQLException {
        gameDAO.clearGames();
        gameDAO.createGame("Game1");
        gameDAO.createGame("Game2");
        gameDAO.createGame("Game3");
        Assertions.assertEquals(3, gameDAO.getAllGames().size());


    }

    @Test
    public void getAllGamesNeg() throws DataAccessException {
        gameDAO.clearGames();
        Assertions.assertEquals(0,gameDAO.getAllGames().size());
    }


    @Test
    public void clearGamesPos() throws DataAccessException, SQLException {
        gameDAO.createGame("TestGame");
        gameDAO.createGame("TestGame2");
        gameDAO.createGame("TestGame3");
        gameDAO.clearGames();

        Assertions.assertEquals(0,gameDAO.getAllGames().size());
    }


    @Test
    public void blackPlayerFreePos() throws SQLException, DataAccessException {
        gameDAO.clearGames();
        gameDAO.createGame("TestGame");
        int id = gameDAO.getGameIDByName("TestGame");
        Assertions.assertTrue(gameDAO.blackPlayerFree(id));
    }

    @Test
    public void blackPlayerFreeNeg() throws SQLException, DataAccessException {
        gameDAO.clearGames();
        gameDAO.createGame("TestGame");
        int id = gameDAO.getGameIDByName("TestGame");
        gameDAO.setBlackUser(id,"Bob");
        Assertions.assertFalse(gameDAO.blackPlayerFree(id));
    }


    @Test
    public void whitePlayerFreePos() throws SQLException, DataAccessException {
        gameDAO.clearGames();
        gameDAO.createGame("TestGame");
        int id = gameDAO.getGameIDByName("TestGame");
        Assertions.assertTrue(gameDAO.whitePlayerFree(id));
    }

    @Test
    public void whitePlayerFreeNeg() throws DataAccessException, SQLException {
        gameDAO.clearGames();
        gameDAO.createGame("TestGame");
        int id = gameDAO.getGameIDByName("TestGame");
        gameDAO.setWhiteUser(id,"Bob");
        Assertions.assertFalse(gameDAO.whitePlayerFree(id));
    }



}
