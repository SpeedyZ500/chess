package dataaccess.gamedao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO{
    //note not writing yet, just using it to implement the logic in the tests
    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData updateGame(GameData gameData) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteGame(Integer gameID) throws DataAccessException {

    }

    @Override
    public void clearGames() throws DataAccessException {

    }

    @Override
    public boolean gameExists(String gameName) {
        return false;
    }
}
