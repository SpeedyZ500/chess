package service;

import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.UserDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class GameServiceTests {
    static final GameDAO gameDAO = new MemoryGameDAO();
    static final UserDAO userDAO = new MemoryUserDAO();
    static final AuthDAO authDAO = new MemoryAuthDAO();
    static final ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);

    @BeforeEach
    void clear() throws ResponseException{
        clearService.clear();
    }
}
