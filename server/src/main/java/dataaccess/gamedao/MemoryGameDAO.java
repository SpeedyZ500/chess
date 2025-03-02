package dataaccess.gamedao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    private int nextId = 1;
    HashMap<Integer, GameData> games = new HashMap<>();
    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        gameData = new GameData(
                nextId++,
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                gameData.game()
        );
        games.put(gameData.gameID(), gameData);
        return gameData;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public GameData updateGame(GameData gameData) throws DataAccessException {
        games.put(gameData.gameID(), gameData);
        return gameData;
    }

    @Override
    public void deleteGame(Integer gameID) throws DataAccessException {
        games.remove(gameID);
    }

    @Override
    public void clearGames() throws DataAccessException {
        games.clear();
    }

    @Override
    public boolean gameExists(String gameName) {
        Iterator<GameData> iter = games.values().iterator();
        while(iter.hasNext()){
            GameData curr = iter.next();
            if(curr.gameName().equals(gameName)){
                return true;
            }
        }
        return false;
    }
}
