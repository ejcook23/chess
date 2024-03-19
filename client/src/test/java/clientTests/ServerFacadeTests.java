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
    }

    @Test
    void registerNeg() throws Exception {
        ServerFacade.wipeDB();
        UserAndAuthResponse response = ServerFacade.register("player4", "pass5word", "p1@em77ail.com");
        assertThrows(Exception, () -> ServerFacade.register("player4", "pass5word", "p1@em77ail.com");
        assertTrue(authData.authToken().length() > 10);
    }



}
