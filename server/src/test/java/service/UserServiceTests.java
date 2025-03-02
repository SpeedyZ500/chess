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
    static final GameDAO gameDAO = new MemoryGameDAO();
    static final UserDAO userDAO = new MemoryUserDAO();
    static final AuthDAO authDAO = new MemoryAuthDAO();
    static final ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);
    static final UserService userService = new UserService(userDAO, authDAO);

    @BeforeEach
    void clear() throws ResponseException{
        clearService.clear();
    }

    @Test
    void register()throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        AuthData authData = userService.register(userData);
        var users = userService.listUsers();
        var auths = userService.listAuths();

        assertEquals(1, users.size());
        assertEquals(1, auths.size());
        assertTrue(users.contains(userData));
        assertTrue(auths.contains(authData));
    }

    @Test
    void noDuplicateUsers()throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        userService.register(userData);
        assertThrows(ResponseException.class, () -> userService.register(userData));
    }

    @Test
    void loginExistingUser()throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        userService.register(userData);
        AuthData authData = userService.login(userData);
        var auths = userService.listAuths();
        assertTrue(auths.contains(authData));
    }
    @Test
    void userMustExist()throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        assertThrows(ResponseException.class, () -> userService.login(userData));
    }
    @Test
    void passwordMustMatch() throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        userService.register(userData);
        var missMatch = new UserData("sonic_the_hedgehog", "got2gofast!", "");

        assertThrows(ResponseException.class, () -> userService.login(missMatch));
    }

    @Test
    void testAuthTokenExists() throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        userService.register(userData);
        AuthData authData = userService.login(userData);
        assertTrue(userService.verifyToken(authData.authToken()));
    }
    @Test
    void logout() throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        userService.register(userData);
        AuthData authData = userService.login(userData);
        userService.logout(authData.authToken());
        assertFalse(userService.verifyToken(authData.authToken()));
    }

    @Test
    void getUsername() throws ResponseException{
        var userData = new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org");
        AuthData authData = userService.register(userData);
        var username = userService.getUsername(authData.authToken());
        assertEquals(username, userData.username());
    }


}
