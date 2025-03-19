package service;

import chess.ChessGame;
import exception.DataAccessException;
import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ClearServiceTest {
    static final GameDAO GAME_DAO = new MemoryGameDAO();
    static final UserDAO USER_DAO = new MemoryUserDAO();
    static final AuthDAO AUTH_DAO = new MemoryAuthDAO();
    static final ClearService CLEAR_SERVICE = new ClearService(AUTH_DAO, USER_DAO, GAME_DAO);
    @BeforeEach
    void add() throws DataAccessException {
        USER_DAO.createUser(new UserData("bill_nye_science", "12345", "billnye@scienceguy.com"));
        USER_DAO.createUser(new UserData("sonic_the_hedgehog", "got2goFast!", "sonichedgehog@sega.org"));
        USER_DAO.createUser(new UserData("ash_catch_em", "peek@U4L!fe", "ash@pokemon.com"));
        AUTH_DAO.createAuth(new AuthData("", "bill_nye_science"));
        AUTH_DAO.createAuth(new AuthData("", "sonic_the_hedgehog"));
        AUTH_DAO.createAuth(new AuthData("", "ash_catch_em"));
        GAME_DAO.createGame(new GameData(0, "", "", "idk", new ChessGame()));
        GAME_DAO.createGame(new GameData(0, "", "", "Chess Masters", new ChessGame()));
        GAME_DAO.createGame(new GameData(0, "", "", "Chess Duels", new ChessGame()));
    }

    @Test
    void clear() throws ResponseException {
        CLEAR_SERVICE.clear();
        try{
            assertEquals(0, AUTH_DAO.listAuths().size());
            assertEquals(0, USER_DAO.listUsers().size());
            assertEquals(0, GAME_DAO.listGames().size());
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }

    }

}
