package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.GameAccess;
import dataAccess.MemGameAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class SQLGameAccessTests {
    GameAccess gameDAO = new MemGameAccess();

    @Test
    public void createGamePos() throws DataAccessException, SQLException {
        gameDAO.clearGames();
        gameDAO.createGame("TestGame");
        Assertions.assertNotNull(gameDAO.getAllGames());
        Assertions.assertTrue(gameDAO.gameExistsByName("TestGame"));
    }

    @Test // NEED TO FIX THIS TEST.. not sure why it's not throwing.
    public void createGameNeg() throws DataAccessException {
        gameDAO.clearGames();
        Assertions.assertThrows(Exception.class, () -> gameDAO.createGame(null));
        Assertions.assertNull(gameDAO.getAllGames());
    }


    @Test
    public void gameExistsByIDPos() throws SQLException, DataAccessException {
        gameDAO.clearGames();
        gameDAO.createGame("TestGame");
        int id = gameDAO.getGameIDByName("TestGame");
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
    public void getGameDataPos() throws DataAccessException {
        gameDAO.clearGames();


    }

    @Test
    public void getGameDataNeg() {

    }


    @Test
    public void getAllGamesPos() {

    }

    @Test
    public void getAllGamesNeg() {

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
    public void blackPlayerFreePos() {

    }

    @Test
    public void blackPlayerFreeNeg() {

    }


    @Test
    public void whitePlayerFreePos() {

    }

    @Test
    public void whitePlayerFreeNeg() {

    }



}
