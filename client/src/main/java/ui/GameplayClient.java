package ui;

import model.GameData;
import server.ServerFacade;

public class GameplayClient implements Client{
    private final String serverUrl;
    private final ServerFacade server;
    private final String username;
    private final String authToken;
    private GameData gameData;
    GameplayClient(String serverUrl, ServerFacade server, String username, String authToken, GameData gameData){
        this.serverUrl = serverUrl;
        this.server = server;
        this.username = username;
        this.authToken = authToken;
        this.gameData = gameData;
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public Client transition(String token) {
        return this;
    }

    @Override
    public Client transition() {
        return new PostloginClient(serverUrl, server, username, authToken);
    }
}
