package dataaccess.gamedao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(GameData gameData) throws DataAccessException;
    Collection<GameData> lisGames() throws DataAccessException;
    GameData getGame(Integer gameID) throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException;
    void deleteGame(Integer gameID) throws DataAccessException;
    void clearGames() throws DataAccessException;
    boolean gameExists(String gameName);

}
