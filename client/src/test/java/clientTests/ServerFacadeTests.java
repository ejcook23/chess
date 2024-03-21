package clientTests;

import model.CreateGameResponse;
import model.UserAndAuthResponse;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;
import facade.ServerFacade;
import spark.utils.Assert;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() throws Exception {
        ServerFacade.wipeDB();
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void registerPos() throws Exception {
        ServerFacade.wipeDB();
        UserAndAuthResponse response = ServerFacade.register("TestPlayer1", "pass5word", "TestPlayer1@em77ail.com");
        assertNotNull(response.authToken());
    }

    @Test
    void registerNeg() throws Exception {
        ServerFacade.wipeDB();
        UserAndAuthResponse response = ServerFacade.register("player4", "pass5word", "p1@em77ail.com");
        Assertions.assertThrows(Exception.class, () -> ServerFacade.register("player4", "pass5word", "p1@em77ail.com"));
    }

    @Test
    void loginPos() throws Exception {
        ServerFacade.wipeDB();
        ServerFacade.register("player4", "pass5word", "p1@em77ail.com");
        UserAndAuthResponse response = ServerFacade.login("player4","pass5word");
        assertNotNull(response.authToken());
    }


    @Test
    void loginNeg() throws Exception {
        ServerFacade.wipeDB();
        Assertions.assertThrows(Exception.class, () -> ServerFacade.login("player4","pass5word"));

    }

    @Test
    void logoutPos() throws Exception {
        ServerFacade.wipeDB();
        ServerFacade.register("player4", "pass5word", "p1@em77ail.com");
        UserAndAuthResponse response = ServerFacade.login("player4","pass5word");
        Assertions.assertNotNull(response);
        Assertions.assertDoesNotThrow(() -> ServerFacade.logout(response.authToken()));
        assertNotNull(response.authToken());
    }


    @Test
    void logoutNeg() throws Exception {
        ServerFacade.wipeDB();
        Assertions.assertThrows(Exception.class, () -> ServerFacade.login("player4","pass5word"));

    }

    @Test
    void createGamePos() throws Exception {
        ServerFacade.wipeDB();
        UserAndAuthResponse response = ServerFacade.register("player4", "pass5word", "p1@em77ail.com");
        Assertions.assertDoesNotThrow(() -> ServerFacade.createGame(response.authToken(),"MyNewGame"));
        Assertions.assertNotNull((response));

    }


    @Test
    void createGameNeg() throws Exception {
        ServerFacade.wipeDB();
        Assertions.assertThrows(Exception.class, () -> ServerFacade.createGame("sdfsdjf5jksf","gameNameHehe"));

    }

    @Test
    void listGamesPos() throws Exception {
        ServerFacade.wipeDB();
        UserAndAuthResponse response = ServerFacade.register("player4", "pass5word", "p1@em77ail.com");
        Assertions.assertDoesNotThrow(() -> ServerFacade.listGames(response.authToken()));
        Assertions.assertNotNull(ServerFacade.listGames(response.authToken()));

    }


    @Test
    void listGamesNeg() throws Exception {
        ServerFacade.wipeDB();
        Assertions.assertThrows(Exception.class, () -> ServerFacade.listGames("3453425safeguard"));

    }

    @Test
    void joinGamePos() throws Exception {
        ServerFacade.wipeDB();
        UserAndAuthResponse response = ServerFacade.register("player4", "pass5word", "p1@em77ail.com");
        CreateGameResponse createResp = ServerFacade.createGame(response.authToken(),"gamey");
        Assertions.assertDoesNotThrow(() -> ServerFacade.joinGame(response.authToken(),"WHITE",createResp.gameID()));
        Assertions.assertDoesNotThrow(() -> ServerFacade.joinGame(response.authToken(),"BLACK",createResp.gameID()));


    }


    @Test
    void joinGameNeg() throws Exception {
        ServerFacade.wipeDB();
        UserAndAuthResponse response = ServerFacade.register("player4", "pass5word", "p1@em77ail.com");
        CreateGameResponse createResp = ServerFacade.createGame(response.authToken(),"gamey");
        Assertions.assertThrows(Exception.class, () -> ServerFacade.joinGame(response.authToken(),"BLUE",createResp.gameID()));

    }

    @Test
    void wipeDBPos() throws Exception {
        ServerFacade.wipeDB();
        UserAndAuthResponse response = ServerFacade.register("player4", "pass5word", "p1@em77ail.com");
        CreateGameResponse createResp = ServerFacade.createGame(response.authToken(),"gamey");
        Assertions.assertDoesNotThrow(() -> ServerFacade.wipeDB());

    }












}
