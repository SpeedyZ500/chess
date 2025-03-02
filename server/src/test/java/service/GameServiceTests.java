package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.UserDAO;

import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class GameServiceTests {
    static final GameDAO gameDAO = new MemoryGameDAO();
    static final UserDAO userDAO = new MemoryUserDAO();
    static final AuthDAO authDAO = new MemoryAuthDAO();
    static final ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);
    static final GameService gameService = new GameService(gameDAO);

    @BeforeEach
    void clear() throws ResponseException{
        clearService.clear();
    }

    @Test
    void getEmptyList() throws ResponseException{
        var result = gameService.listGames();
        assertEquals(0, result.size());
    }

    @Test
    void createGame() throws ResponseException{
        var gameName = "new game";
        assertDoesNotThrow(() -> gameService.createGame(gameName));
    }

    @Test
    void duplicateGame() throws ResponseException{
        var gameName = "new game";
        gameService.createGame(gameName);
        assertThrows(ResponseException.class, () -> gameService.createGame(gameName));
    }

    @Test
    void getListOfGames() throws ResponseException{
        gameService.createGame("new game");
        gameService.createGame("My Game");
        var result = gameService.listGames();
        assertEquals(2, result.size());
    }

    @Test
    void joinGame() throws ResponseException{
        var gameName = "new game";
        var gameID = gameService.createGame(gameName);
        var expected = gameService.joinGame("sonic_the_hedgehog", "WHITE", gameID);
        var actual = gameService.getGame(expected.gameID());
        assertGameDataEqual(expected, actual);

        expected = gameService.joinGame("shadow_the_hedgehog", "BLACK", expected.gameID());
        actual = gameService.getGame(expected.gameID());
        assertGameDataEqual(expected, actual);
    }

    @Test
    void badJoinGame() throws ResponseException{
        var gameName = "new game";
        var gameID = gameService.createGame(gameName);
        gameService.joinGame("sonic_the_hedgehog", "WHITE", gameID);

        assertThrows(ResponseException.class, () -> gameService.joinGame("shadow_the_hedgehog", "WHITE", gameID));
    }
    @Test
    void notColor() throws ResponseException{
        var gameName = "new game";
        var gameID = gameService.createGame(gameName);
        assertThrows(ResponseException.class, () -> gameService.joinGame("shadow_the_hedgehog", "yellow", gameID));
    }
    @Test
    void noName() throws ResponseException{
        var gameName = "new game";
        var gameID = gameService.createGame(gameName);
        assertThrows(ResponseException.class, () -> gameService.joinGame("", "WHITE", gameID));
    }
    @Test
    void noGame() throws ResponseException{
        assertThrows(ResponseException.class, () -> gameService.joinGame("shadow_the_hedgehog", "WHITE", 1));
    }

    @Test
    void updateGame() throws ResponseException{
        var move = new ChessMove(new ChessPosition(2,1), new ChessPosition(3,1), null);
        var gameID = gameService.createGame("new game");
        var gameData = gameService.getGame(gameID);
        ChessGame game = gameData.game();
        assertDoesNotThrow(() -> game.makeMove(move));
        var expected = gameService.updateGame(
                new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game)
        );
        var actual = gameService.getGame(gameData.gameID());
        assertGameDataEqual(expected, actual);
    }


    public static void assertGameDataEqual(GameData expected, GameData actual){
        assertEquals(expected.gameID(), actual.gameID());
        assertEquals(expected.whiteUsername(), actual.whiteUsername());
        assertEquals(expected.blackUsername(), actual.blackUsername());
        assertEquals(expected.gameName(), actual.gameName());
        assertEquals(expected.game(), actual.game());
    }


}
