package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.gamedao.GameDAO;
import exception.ResponseException;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    public GameService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }
    public Collection<GameData> listGames() throws ResponseException {
        try{
            return this.gameDAO.listGames();
        }
        catch(DataAccessException e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    public int createGame(String gameName) throws ResponseException{
        try{
            if(gameName == null || gameDAO.gameExists(gameName)){
                throw new ResponseException(400, "Error: bad request");
            }
            return gameDAO.createGame(new GameData(0, null,null,gameName, new ChessGame())).gameID();
        }
        catch(DataAccessException e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    public GameData updateGame(GameData gameData) throws ResponseException{
        try{
            if(gameData.game() == null || gameData.gameName() == null){
                throw new ResponseException(400, "Error: bad request");
            }
            return gameDAO.updateGame(gameData);
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    public GameData joinGame(String username, String playerColor, int gameID) throws ResponseException{
        try{
            GameData gameData = gameDAO.getGame(gameID);
            if(
                    username == null || username.isEmpty()
                    || playerColor == null ||
                    !(playerColor.equalsIgnoreCase("black") || playerColor.equalsIgnoreCase("white"))
                    || gameDAO.getGame(gameID) == null
            ){
                throw new ResponseException(400, "Error: bad request");
            }
            if(
                    (playerColor.equalsIgnoreCase("BLACK") && gameData.blackUsername() != null)
                    || (playerColor.equalsIgnoreCase("WHITE") && gameData.whiteUsername() != null)
            ){
                throw new ResponseException(403, "Error: already taken");
            }
            return this.updateGame(new GameData(
                    gameData.gameID(),
                    playerColor.equalsIgnoreCase("WHITE") ? username : gameData.whiteUsername(),
                    playerColor.equalsIgnoreCase("BLACK") ? username : gameData.blackUsername(),
                    gameData.gameName(),
                    gameData.game()
                    ));
        }
        catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws ResponseException{
        try{
            return gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }


}
