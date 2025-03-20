package ui;

import model.GameData;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.List;

public class PostloginClient implements Client{
    private final String serverUrl;
    private final ServerFacade server;
    private final String authToken;
    private final String username;
    List<GameData> games;
    public PostloginClient(String serverUrl, ServerFacade server, String username, String authToken){
        this.serverUrl = serverUrl;
        this.server = server;
        this.authToken = authToken;
        this.username = username;
        this.games = new ArrayList<>();
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public Client transition(String token) {
        int gameIndex = Integer.parseInt(token);
        GameData game = games.get(gameIndex);
        return new GameplayClient(serverUrl, server, username, authToken, game);
    }

    @Override
    public Client transition() {
        return new PreloginClient(serverUrl, server);
    }
}
