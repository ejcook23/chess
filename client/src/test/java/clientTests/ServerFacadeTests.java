package clientTests;

import model.UserAndAuthResponse;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;
import facade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
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
        assertThrows(Exception.class, () -> ServerFacade.register("player4", "pass5word", "p1@em77ail.com"));
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
        assertThrows(Exception.class, () -> ServerFacade.login("player4","pass5word"));

    }



}
