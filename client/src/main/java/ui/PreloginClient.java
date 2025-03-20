package ui;

import exception.ResponseException;
import model.AuthData;
import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.RESET;

public class PreloginClient implements Client {
    private final String serverUrl;
    private final ServerFacade server;
    public PreloginClient(String serverUrl){
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
    }
    public PreloginClient(String serverUrl, ServerFacade server){
        this.serverUrl = serverUrl;
        this.server = server;
    }

    @Override
    public String eval(String input){
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd){
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        }
        catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    String register(String ...params) throws ResponseException {
        if(params.length >= 3){
            AuthData authData = server.register(params[0], params[1], params[3]);
            return RESET + String.format("transition; %s, %s; Logged in as %s",
                    authData.authToken(), authData.username(), authData.username());
        }
        else{
            throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
        }
    }


    @Override
    public String help() {
        return "";
    }

    @Override
    public Client transition(String token) {
        String[] parsed = token.split(",");
        return new PostloginClient(serverUrl, server, parsed[0], parsed[1]);
    }

    @Override
    public Client transition() {
        return this;
    }
    @Override
    public String terminalState(){
        return "[LOGGED_OUT]";
    }
}
