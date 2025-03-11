package service;

import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.UserDAO;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    static final GameDAO GAME_DAO = new MemoryGameDAO();
    static final UserDAO USER_DAO = new MemoryUserDAO();
    static final AuthDAO AUTH_DAO = new MemoryAuthDAO();
    static final ClearService CLEAR_SERVICE = new ClearService(AUTH_DAO, USER_DAO, GAME_DAO);
    static final UserService USER_SERVICE = new UserService(USER_DAO, AUTH_DAO);

    @BeforeEach
    void clear() throws ResponseException{
        CLEAR_SERVICE.clear();
    }

    @Test
    void register()throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        AuthData authData = USER_SERVICE.register(userData);
        var users = USER_SERVICE.listUsers();
        var auths = USER_SERVICE.listAuths();

        assertEquals(1, users.size());
        assertEquals(1, auths.size());
    }

    @Test
    void noDuplicateUsers()throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        USER_SERVICE.register(userData);
        assertThrows(ResponseException.class, () -> USER_SERVICE.register(userData));
    }

    @Test
    void loginExistingUser()throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        USER_SERVICE.register(userData);
        AuthData authData = USER_SERVICE.login(userData);
        var auths = USER_SERVICE.listAuths();
        assertTrue(auths.contains(authData));
    }
    @Test
    void userMustExist()throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        assertThrows(ResponseException.class, () -> USER_SERVICE.login(userData));
    }
    @Test
    void passwordMustMatch() throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        USER_SERVICE.register(userData);
        var missMatch = new UserData("sonic_the_hedgehog", "got2gofast!", "");

        assertThrows(ResponseException.class, () -> USER_SERVICE.login(missMatch));
    }

    @Test
    void testAuthTokenExists() throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        USER_SERVICE.register(userData);
        AuthData authData = USER_SERVICE.login(userData);
        assertTrue(USER_SERVICE.verifyToken(authData.authToken()));
    }
    @Test
    void logout() throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        USER_SERVICE.register(userData);
        AuthData authData = USER_SERVICE.login(userData);
        USER_SERVICE.logout(authData.authToken());
        assertFalse(USER_SERVICE.verifyToken(authData.authToken()));
    }

    


}
