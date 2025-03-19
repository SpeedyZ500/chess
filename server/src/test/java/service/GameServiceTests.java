package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.GameDAOTests;
import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.UserDAO;

import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class GameServiceTests {
    static final GameDAO GAME_DAO = new MemoryGameDAO();
    static final UserDAO USER_DAO = new MemoryUserDAO();
    static final AuthDAO AUTH_DAO = new MemoryAuthDAO();
    static final ClearService CLEAR_SERVICE = new ClearService(AUTH_DAO, USER_DAO, GAME_DAO);
    static final GameService GAME_SERVICE = new GameService(GAME_DAO);

    @BeforeEach
    void clear() throws ResponseException {
        CLEAR_SERVICE.clear();
    }

    @Test
    void getEmptyList() throws ResponseException{
        var result = GAME_SERVICE.listGames();
        assertEquals(0, result.size());
    }

    @Test
    void createGame() throws ResponseException{
        var gameName = "new game";
        assertDoesNotThrow(() -> GAME_SERVICE.createGame(gameName));
    }

    @Test
    void duplicateGame() throws ResponseException{
        var gameName = "new game";
        GAME_SERVICE.createGame(gameName);
        assertThrows(ResponseException.class, () -> GAME_SERVICE.createGame(gameName));
    }

    @Test
    void getListOfGames() throws ResponseException{
        GAME_SERVICE.createGame("new game");
        GAME_SERVICE.createGame("My Game");
        var result = GAME_SERVICE.listGames();
        assertEquals(2, result.size());
    }

    @Test
    void joinGame() throws ResponseException{
        var gameName = "new game";
        var gameID = GAME_SERVICE.createGame(gameName);
        var expected = GAME_SERVICE.joinGame("sonic_the_hedgehog", "WHITE", gameID);
        var actual = GAME_SERVICE.getGame(expected.gameID());
        GameDAOTests.assertGameDataEqual(expected, actual);

        expected = GAME_SERVICE.joinGame("shadow_the_hedgehog", "BLACK", expected.gameID());
        actual = GAME_SERVICE.getGame(expected.gameID());
        GameDAOTests.assertGameDataEqual(expected, actual);
    }

    @Test
    void badJoinGame() throws ResponseException{
        var gameName = "new game";
        var gameID = GAME_SERVICE.createGame(gameName);
        GAME_SERVICE.joinGame("sonic_the_hedgehog", "WHITE", gameID);

        assertThrows(ResponseException.class, () -> GAME_SERVICE.joinGame("shadow_the_hedgehog", "WHITE", gameID));
    }
    @Test
    void notColor() throws ResponseException{
        var gameName = "new game";
        var gameID = GAME_SERVICE.createGame(gameName);
        assertThrows(ResponseException.class, () -> GAME_SERVICE.joinGame("shadow_the_hedgehog", "yellow", gameID));
    }
    @Test
    void noName() throws ResponseException{
        var gameName = "new game";
        var gameID = GAME_SERVICE.createGame(gameName);
        assertThrows(ResponseException.class, () -> GAME_SERVICE.joinGame("", "WHITE", gameID));
    }
    @Test
    void noGame() throws ResponseException{
        assertThrows(ResponseException.class, () -> GAME_SERVICE.joinGame("shadow_the_hedgehog", "WHITE", 1));
    }

    @Test
    void updateGame() throws ResponseException{
        var move = new ChessMove(new ChessPosition(2,1), new ChessPosition(3,1), null);
        var gameID = GAME_SERVICE.createGame("new game");
        var gameData = GAME_SERVICE.getGame(gameID);
        ChessGame game = gameData.game();
        assertDoesNotThrow(() -> game.makeMove(move));
        var expected = GAME_SERVICE.updateGame(
                new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game)
        );
        var actual = GAME_SERVICE.getGame(gameData.gameID());
        GameDAOTests.assertGameDataEqual(expected, actual);
    }


}
