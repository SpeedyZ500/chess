package server;

import com.google.gson.Gson;
import gson.GsonConfig;
import model.GameData;
import service.GameService;
import exception.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class GameHandler {
    UserService userService;
    GameService gameService;
    private static final Gson GSON = GsonConfig.createGson();;

    public GameHandler(UserService userService, GameService gameService){
        this.userService = userService;
        this.gameService = gameService;
    }

    public Object listGames(Request req, Response res) throws ResponseException {
        record GamesList(Collection<GameData> games){}
        GamesList gameList = new GamesList(gameService.listGames());
        res.status(200);
        res.body(GSON.toJson(gameList));
        return res.body();
    }

    public Object createGame(Request req, Response res) throws ResponseException {
        record GameName(String gameName) {};
        var gameName = new Gson().fromJson(req.body(), GameName.class);
        if(gameName.gameName() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        int gameID = gameService.createGame(gameName.gameName());
        res.status(200);
        res.body("{\"gameID\": %d}".formatted(gameID));
        return res.body();
    }

    public Object joinGame(Request req, Response res) throws ResponseException{
        String authToken = req.headers("authorization");
        String username = userService.getUsername(authToken);
        record JoinGameRequest(String playerColor, int gameID) {
        }
        JoinGameRequest joinData = new Gson().fromJson(req.body(), JoinGameRequest.class);
        gameService.joinGame(username, joinData.playerColor(), joinData.gameID());
        res.status(200);
        return "";
    }

}
