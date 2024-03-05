package serviceTests;

import dataAccess.*;
import service.GameService;

public class ListGamesTests {
    AuthAccess authDAO = new MemAuthAccess();
    GameAccess gameDAO = new MemGameAccess();
    UserAccess userDAO = new MemUserAccess();

    GameService service = new GameService(authDAO, gameDAO, userDAO);



}
