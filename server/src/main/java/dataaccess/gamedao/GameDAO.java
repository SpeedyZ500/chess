package dataaccess.gamedao;

import exception.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(GameData gameData) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    GameData getGame(Integer gameID) throws DataAccessException;
    GameData updateGame(GameData gameData) throws DataAccessException;
    void deleteGame(Integer gameID) throws DataAccessException;
    void clearGames() throws DataAccessException;
    boolean gameExists(String gameName) throws DataAccessException;

}
