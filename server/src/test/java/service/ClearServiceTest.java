package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ClearServiceTest {
    static final GameDAO gameDAO = new MemoryGameDAO();
    static final UserDAO userDAO = new MemoryUserDAO();
    static final AuthDAO authDAO = new MemoryAuthDAO();
    static final ClearService service = new ClearService(authDAO, userDAO, gameDAO);
    @BeforeEach
    void add() throws DataAccessException {
        userDAO.createUser(new UserData("bill_nye_science", "12345", "billnye@scienceguy.com"));
        userDAO.createUser(new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org"));
        userDAO.createUser(new UserData("ash_catch_em", "peek@U4L!fe", "ash@pokemon.com"));
        authDAO.createAuth(new AuthData("", "bill_nye_science"));
        authDAO.createAuth(new AuthData("", "sonic_the_hedgehog"));
        authDAO.createAuth(new AuthData("", "ash_catch_em"));
        gameDAO.createGame(new GameData(0, "", "", "idk", new ChessGame()));
        gameDAO.createGame(new GameData(0, "", "", "Chess Masters", new ChessGame()));
        gameDAO.createGame(new GameData(0, "", "", "Chess Duels", new ChessGame()));
    }

    @Test
    void clear() throws ResponseException{
        service.clear();
        try{
            assertEquals(0, authDAO.listAuths().size());
            assertEquals(0, userDAO.listUsers().size());
            assertEquals(0, gameDAO.listGames().size());
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }

    }

}
