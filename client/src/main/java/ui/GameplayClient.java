package ui;

import chess.ChessBoard;
import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.RESET;

public class GameplayClient implements Client{
    private final String serverUrl;
    private final ServerFacade server;
    private final String username;
    private final String authToken;
    private ChessGame game;
    private final ChessGame.TeamColor team;
    private final int gameID;

    GameplayClient(String serverUrl, ServerFacade server, String username, String authToken, GameData gameData){
        this.serverUrl = serverUrl;
        this.server = server;
        this.username = username;
        this.authToken = authToken;
        this.game = gameData.game();
        this.gameID = gameData.gameID();
        this.team = gameData.blackUsername().equals(username) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }

    @Override
    public String eval(String input){
        return "transition; How did you get here? This isn't even implemented yet.";
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

    public String drawBoard(){
        return drawBoard(game.getBoard(), team);
    }
    public static String drawBoard(ChessBoard board){
        return drawBoard(board, ChessGame.TeamColor.WHITE);
    }

    public static String drawBoard(ChessBoard board, ChessGame.TeamColor color){
        return "";
    }


    @Override
    public String terminalState(){
        return drawBoard() + "\n" + RESET  + String.format("[GamePlay]");
    }
}
