package ui;

import chess.ChessGame;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import model.GameData;
import client.ServerFacade;

import static ui.EscapeSequences.*;

public class GameplayClient implements Client{
    private final String serverUrl;
    private final NotificationHandler notifier;
    private final WebSocketFacade webSocket;
    private final String username;
    private final String authToken;
    private ChessGame game;
    private final ChessGame.TeamColor team;
    private final int gameID;
    private final BoardPrinter boardPrinter;

    GameplayClient(String serverUrl, NotificationHandler notifier, String username, String authToken, GameData gameData) throws ResponseException {
        this.serverUrl = serverUrl;
        this.notifier = notifier;
        this.webSocket = new WebSocketFacade(serverUrl, notifier);
        this.username = username;
        this.authToken = authToken;
        this.game = gameData.game();
        this.gameID = gameData.gameID();
        this.team = gameData.blackUsername().equals(username) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        this.boardPrinter = new BoardPrinter(gameData.game());
    }

    @Override
    public String eval(String input){
        if(game == null || gameID < 0){
            return "transition;; How did you get here? You don't even have a game saved.";
        }
        return "transition;; How did you get here? This isn't even implemented yet.";
    }

    @Override
    public String help() {
        return String.format("""
                    %s create <NAME> %s - a game
                    %s list %s - games
                    %s join <ID> [WHITE|BLACK] %s - a game
                    %s observe <ID> %s - a game
                    %s logout %s - when your done
                    %s quit %s - playing chess
                    %s help %s - with commands
                """,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW
                ,SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_YELLOW
        );
    }

    @Override
    public Client transition(String token) {
        return this;
    }

    @Override
    public Client transition() {
        return new PostloginClient(serverUrl, notifier, username, authToken);
    }




    @Override
    public String terminalState(){
        return boardPrinter.print(team) + "\n" + RESET  + "[IN-GAME]";
    }
}
