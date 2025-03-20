package client;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    @AfterEach
    public void clear() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    public String registrationHelper() throws ResponseException{
        AuthData result = facade.register("username", "password", "user@email.com");
        return result.authToken();
    }

    @Test
    public void registerPositive() throws ResponseException{
        Assertions.assertDoesNotThrow(this::registrationHelper);
    }

    @Test
    public void registerNegative() throws ResponseException{
        registrationHelper();
        Assertions.assertThrows(ResponseException.class, this::registrationHelper);
    }

}
