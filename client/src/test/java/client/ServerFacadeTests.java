package client;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

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
    public void registerPositive(){
        Assertions.assertDoesNotThrow(this::registrationHelper);
    }

    @Test
    public void registerNegative() throws ResponseException{
        registrationHelper();
        Assertions.assertThrows(ResponseException.class, this::registrationHelper);
    }

    @Test
    public void logoutPositive() throws ResponseException {
        String authToken = registrationHelper();
        Assertions.assertDoesNotThrow(() -> facade.logout(authToken));
    }

    @Test
    public void logoutNegative() throws ResponseException {
        String authToken = registrationHelper();
        Assertions.assertThrows(ResponseException.class, () -> facade.logout(authToken + "badData"));
    }

    @Test
    public void loginPositive() throws ResponseException {
        registrationHelper();
        Assertions.assertDoesNotThrow(() -> facade.login("username", "password"));
    }

    @Test
    public void loginNegatives() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> facade.login("username", "password"));
        registrationHelper();
        Assertions.assertThrows(ResponseException.class, () -> facade.login("username", "p4ssword"));
    }

    public int createGame(String authToken) throws ResponseException{
        return facade.createGame("newGame", authToken);
    }

    @Test
    public void createGamePositive() throws ResponseException{
        String authToken = registrationHelper();
        Assertions.assertDoesNotThrow(() -> createGame(authToken));
    }

    @Test
    public void createGameNegatives() throws ResponseException{
        String authToken = registrationHelper();
        Assertions.assertThrows(ResponseException.class, () -> createGame(authToken + "badData"));
        createGame(authToken);
        Assertions.assertThrows(ResponseException.class,() -> createGame(authToken));
    }

    @Test
    public void listGamesPositive() throws ResponseException{
        String authToken = registrationHelper();
        Assertions.assertDoesNotThrow(() -> facade.listGames(authToken));
        createGame(authToken);
        Assertions.assertDoesNotThrow(() -> facade.listGames(authToken));
        Assertions.assertEquals(1, facade.listGames(authToken).length);
    }

    @Test
    public void listGamesNegative() throws ResponseException{
        String authToken = registrationHelper();
        createGame(authToken);
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames(authToken + "bad"));
    }

    @Test
    public void joinGamePositive() throws ResponseException{
        String authToken = registrationHelper();
        int gameID = createGame(authToken);
        Assertions.assertDoesNotThrow(() -> facade.joinGame("WHITE", gameID, authToken));
        Assertions.assertDoesNotThrow(() -> facade.joinGame("BLACK", gameID, authToken));
    }

    @Test
    public void joinGameNegative() throws ResponseException{
        String authToken = registrationHelper();
        Assertions.assertThrows(ResponseException.class,
                () -> facade.joinGame("WHITE", -1, authToken));
        int gameID = createGame(authToken);
        Assertions.assertThrows(ResponseException.class,
                () -> facade.joinGame("WHITE", gameID, authToken+"bad"));

        facade.joinGame("WHITE", gameID, authToken);
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame("WHITE", gameID, authToken));
    }




}
