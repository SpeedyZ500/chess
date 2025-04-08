package ui;

import client.websocket.NotificationHandler;
import exception.ResponseException;
import model.AuthData;
import client.ServerFacade;

import java.util.Arrays;


import static ui.EscapeSequences.*;

public class PreloginClient implements Client {
    private final String serverUrl;
    private final NotificationHandler notifier;
    private final ServerFacade server;
    public PreloginClient(String serverUrl, NotificationHandler notifier){
        this.serverUrl = serverUrl;
        this.notifier = notifier;
        this.server = new ServerFacade(serverUrl);
    }


    @Override
    public String eval(String input){
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd){
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        }
        catch (ResponseException ex) {
            return SET_TEXT_COLOR_RED + ex.getMessage();
        }
    }

    String login(String ...params) throws ResponseException{
        if(params.length >= 2){
            AuthData authData = server.login(params[0], params[1]);
            return String.format("transition ;; %s, %s ;; Logged in as %s",
                    authData.username(), authData.authToken(), authData.username());
        }
        else{
            throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
        }
    }

    String register(String ...params) throws ResponseException {
        if(params.length >= 3){
            AuthData authData = server.register(params[0], params[1], params[2]);
            return String.format("transition ;; %s, %s ;; Logged in as %s",
                    authData.username(), authData.authToken(), authData.username());
        }
        else{
            throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
        }
    }


    @Override
    public String help() {
        return String.format("""
                    %s register <USERNAME> <PASSWORD> <EMAIL> %s- to create an account
                    %s login <USERNAME> <PASSWORD> %s- to play chess
                    %s quit %s- playing chess
                    %s help %s- with possible commands
                """,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW
                ,SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW
        );
    }

    @Override
    public Client transition(String token) {
        String[] parsed = token.split(",");
        return new PostloginClient(serverUrl, notifier, parsed[0].trim(), parsed[1].trim());
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
