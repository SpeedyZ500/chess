package dataaccess;
//really wanted to put this in the gamedao subpackage,
// but it wouldn't let me because the methods for DatabaseManager are default

import com.google.gson.Gson;
import dataaccess.gamedao.GameDAO;
import gson.GsonConfig;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO extends SQLDataAccess implements GameDAO {
    private final Gson gson;

    public SQLGameDAO() throws DataAccessException {
        configureDatabase(createStatements);
        gson = GsonConfig.createGson();

    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO game (gameName, json) VALUES (?, ?)";
        var json = gson.toJson(gameData);
        var id = executeUpdate(statement, gameData.gameName(), json);
        return new GameData(id, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, json FROM game";
            try (var ps = conn.prepareStatement(statement)){
                try(var rs = ps.executeQuery()){
                    while(rs.next()){
                        result.add(readGame(rs));
                    }
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, json FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()){
                        return readGame(rs);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public GameData updateGame(GameData gameData) throws DataAccessException {
        if(getGame(gameData.gameID()) == null){
            throw new DataAccessException("Game does not Exist");
        }
        var statement = "UPDATE game SET json=? WHERE  gameID=?";
        var json = gson.toJson(gameData);
        executeUpdate(statement, json, gameData.gameID());
        return gameData;
    }

    @Override
    public void deleteGame(Integer gameID) throws DataAccessException {
        if(getGame(gameID) == null){
            throw new DataAccessException("Game does not Exist");
        }
        var statement = "DELETE FROM game WHERE  gameID=?";
        executeUpdate(statement, gameID);
    }

    @Override
    public void clearGames() throws DataAccessException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    @Override
    public boolean gameExists(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT COUNT(*) FROM game WHERE gameName=?";
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, gameName);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()){
                        return rs.getInt(1) > 0;
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return false;
    }

    private GameData readGame(ResultSet rs) throws SQLException{
        var id = rs.getInt("gameID");
        var json = rs.getString("json");
        var game = gson.fromJson(json, GameData.class);
        return game.setId(id);
    }



    private final String[] createStatements = {
            """
            CREATE TABLE if NOT EXISTS game(
                gameID int NOT NULL AUTO_INCREMENT,
                gameName VARCHAR(256) NOT NULL UNIQUE,
                json TEXT DEFAULT NULL,
                PRIMARY KEY (gameID),
                INDEX(gameName)
            )
            """
    };

}
