package server;

import com.google.gson.Gson;
import model.GameData;
import model.JoinGameRequest;
import service.GameService;
import exception.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class GameHandler {
    UserService userService;
    GameService gameService;
    public GameHandler(UserService userService, GameService gameService){
        this.userService = userService;
        this.gameService = gameService;
    }

    public Object listGames(Request req, Response res) throws ResponseException {
        record GamesList(Collection<GameData> games){}
        GamesList gameList = new GamesList(gameService.listGames());
        res.status(200);
        res.body(new Gson().toJson(gameList));
        return res.body();
    }

    public Object createGame(Request req, Response res) throws ResponseException {
        GameData gameData = new Gson().fromJson(req.body(), GameData.class);
        if(gameData.gameName() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        int gameID = gameService.createGame(gameData.gameName());
        res.status(200);
        res.body("{\"gameID\": %d}".formatted(gameID));
        return res.body();
    }

    public Object joinGame(Request req, Response res) throws ResponseException{
        String authToken = req.headers("authorization");
        String username = userService.getUsername(authToken);
        JoinGameRequest joinData = new Gson().fromJson(req.body(), JoinGameRequest.class);
        gameService.joinGame(username, joinData.playerColor(), joinData.gameID());
        res.status(200);
        return "";
    }

}
