package ui;

import exception.ResponseException;
import model.GameData;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
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
    public String eval(String input){
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd){
                case "logout" -> logout();
                default -> help();
            };
        }
        catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String logout() throws ResponseException{
        server.logout(authToken);
        return "transition; Good by";
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

    @Override
    public String terminalState(){
        return "[LOGGED_IN]";
    }
}
