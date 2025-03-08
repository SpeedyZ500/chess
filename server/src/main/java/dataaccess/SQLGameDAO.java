package dataaccess;
//really wanted to put this in the gamedao subpackage,
// but it wouldn't let me because the methods for DatabaseManager are default

import dataaccess.gamedao.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
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
