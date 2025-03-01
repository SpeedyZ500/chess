package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.gamedao.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class GameDAOTests {

    private GameDAO getGameDAO(Class<? extends GameDAO> gameDatabaseClass) throws DataAccessException{
        GameDAO db;
        if(gameDatabaseClass.equals(SQLGameDAO.class)){
            db = new SQLGameDAO();
        }
        else{
            db = new MemoryGameDAO();
        }
        db.clearGames();
        return db;
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class})
    void createGame(Class<? extends GameDAO> dbClass) throws DataAccessException {
        GameDAO gameDAO = getGameDAO(dbClass);
        var gameData = new GameData(0, "", "", "idk", new ChessGame());
        assertDoesNotThrow(() -> gameDAO.createGame(gameData));
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class})
    void sameName(Class<? extends GameDAO> dbClass) throws DataAccessException {
        GameDAO gameDAO = getGameDAO(dbClass);
        var gameData = new GameData(0, "", "", "idk", new ChessGame());
        gameDAO.createGame(gameData);
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(gameData));
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class})
    void getGame(Class<? extends GameDAO> dbClass) throws DataAccessException{
        GameDAO gameDAO = getGameDAO(dbClass);
        var expected = gameDAO.createGame(new GameData(0, "", "", "idk", new ChessGame()));
        var actual = gameDAO.getGame(expected.gameID());
        assertGameDataEqual(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class})
    void listGames(Class<? extends GameDAO> dbClass) throws DataAccessException{
        GameDAO gameDAO = getGameDAO(dbClass);
        List<GameData> expected = new ArrayList<>();
        expected.add(gameDAO.createGame(new GameData(0, "", "", "idk", new ChessGame())));
        expected.add(gameDAO.createGame(new GameData(0, "", "", "Chess Masters", new ChessGame())));
        expected.add(gameDAO.createGame(new GameData(0, "", "", "Chess Duels", new ChessGame())));

        var actual = gameDAO.listGames();
        assertGameCollectionEqual(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class})
    void deleteGame(Class<? extends GameDAO> dbClass) throws DataAccessException{
        GameDAO gameDAO = getGameDAO(dbClass);
        List<GameData> expected = new ArrayList<>();
        var deleteGame = gameDAO.createGame(new GameData(0, "", "", "idk", new ChessGame()));
        expected.add(gameDAO.createGame(new GameData(0, "", "", "Chess Masters", new ChessGame())));
        expected.add(gameDAO.createGame(new GameData(0, "", "", "Chess Duels", new ChessGame())));

        gameDAO.deleteGame(deleteGame.gameID());

        var actual = gameDAO.listGames();
        assertGameCollectionEqual(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class})
    void clearGames(Class<? extends GameDAO> dbClass) throws DataAccessException{
        GameDAO gameDAO = getGameDAO(dbClass);
        gameDAO.createGame(new GameData(0, "", "", "idk", new ChessGame()));
        gameDAO.createGame(new GameData(0, "", "", "Chess Masters", new ChessGame()));
        gameDAO.createGame(new GameData(0, "", "", "Chess Duels", new ChessGame()));
        gameDAO.clearGames();
        var actual = gameDAO.listGames();
        assertEquals(0, actual.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MemoryGameDAO.class})
    void updateGame(Class<? extends GameDAO> dbClass) throws DataAccessException{
        GameDAO gameDAO = getGameDAO(dbClass);
        var gameData = gameDAO.createGame(new GameData(0, "", "", "idk", new ChessGame()));
        var whiteUsername = "sonic_the_hedgehog";
        var blackUsername = "shadow_the_hedgehog";
        var move = new ChessMove(new ChessPosition(2,1), new ChessPosition(3,1), null);
        gameData = gameDAO.getGame(gameData.gameID());

        //test update white username or add white username
        var expected = gameDAO.updateGame(
                new GameData(gameData.gameID(), whiteUsername, gameData.blackUsername(), gameData.gameName(), gameData.game())
        );
        var actual = gameDAO.getGame(gameData.gameID());
        assertGameDataEqual(expected, actual);

        //test update black username or add black username
        gameData = gameDAO.getGame(gameData.gameID());
        expected = gameDAO.updateGame(
                new GameData(gameData.gameID(), gameData.whiteUsername(), blackUsername, gameData.gameName(), gameData.game())
        );
        actual = gameDAO.getGame(gameData.gameID());
        assertGameDataEqual(expected, actual);

        //tests update game, or really make a move
        gameData = gameDAO.getGame(gameData.gameID());
        ChessGame game = gameData.game();
        assertDoesNotThrow(() -> game.makeMove(move));
        expected = gameDAO.updateGame(
                new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game)
        );
        actual = gameDAO.getGame(gameData.gameID());
        assertGameDataEqual(expected, actual);
    }



    public static void assertGameDataEqual(GameData expected, GameData actual){
        assertEquals(expected.gameID(), actual.gameID());
        assertEquals(expected.whiteUsername(), actual.whiteUsername());
        assertEquals(expected.blackUsername(), actual.blackUsername());
        assertEquals(expected.gameName(), actual.gameName());
        assertEquals(expected.game(), actual.game());
    }

    public static void assertGameCollectionEqual(Collection<GameData> excepted, Collection<GameData> actual){
        GameData[] actualList = actual.toArray(new GameData[]{});
        GameData[] expectedList = excepted.toArray(new GameData[]{});
        assertEquals(expectedList.length, actualList.length);
        for (var i = 0; i < actualList.length; i++){
            assertGameDataEqual(expectedList[i], actualList[i]);
        }

    }
}
