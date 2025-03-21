package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;

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
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> quit();
                default -> help();
            };
        }
        catch (ResponseException ex) {
            return SET_TEXT_COLOR_RED + ex.getMessage();
        }
    }
    public String quit(){
        try{
            server.logout(authToken);
            return "quit";
        }
        catch (ResponseException e){
            return "quit";
        }
    }

    public String create(String ...params) throws ResponseException{
        if(params.length >= 1){
            server.createGame(params[0], authToken);
            return "Successfully created game";
        }
        else{
            throw new ResponseException(400, "Expected: <NAME>");
        }
    }

    public String list() throws ResponseException{
        games.clear();
        games.addAll(Arrays.asList(server.listGames(authToken)));
        return "Games:\n" + printListOfGames();
    }

    private String printListOfGames(){
        StringBuilder output = new StringBuilder();
        output.append(String.format("%s| %-6s | %-11s | %-15s | %-15s |%n%s",
                SET_TEXT_UNDERLINE, "ID", "Game Name", "White User", "Black User", RESET_TEXT_UNDERLINE));
        for(int i = 0; i < games.size(); i++){
            GameData game = games.get(i);
            output.append(String.format("| %s%-6d | %s%s-11s | %s%-15s | %s%-15s |%n",
                    SET_TEXT_COLOR_YELLOW,
                    i,
                    SET_TEXT_COLOR_GREEN,
                    game.gameName(),
                    SET_TEXT_COLOR_LIGHT_GREY,
                    game.whiteUsername(),
                    SET_TEXT_COLOR_BLUE,
                    game.blackUsername())
            );

        }
        output.append(SET_TEXT_COLOR_GREEN);
        return output.toString();
    }

    public String join(String ...params) throws ResponseException{
        if (params.length >= 2) {
            int gameID = Integer.parseInt(params[0]);
            GameData game = checkGame(gameID);
            if(!params[1].equals("WHITE") && !params[1].equals("BLACK")){
                throw new ResponseException(400, "Expected: [WHITE|BLACK]");
            }

            server.joinGame(params[1], game.gameID(), authToken);
            return String.format(
                    """
                        Successfully joined: %s. %s However, %s the Gameplay aspect has yet to be implemented.
                        Instead enjoy this lovely recreation of the board, from the perspective you would have in game.
                        %s
                    """,
                    game.gameName(),
                    SET_TEXT_ITALIC,
                    RESET_TEXT_ITALIC,
                    BoardPrinter.print(ChessGame.TeamColor.valueOf(params[1]), game.game().getBoard())
            );
        }
        else {
            throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");
        }
    }

    private GameData checkGame(int gameID) throws ResponseException{
        if(games.isEmpty()){
            throw new ResponseException(400, String.format("""
                        I know you don't know what game that is, no joining/observing random games.
                        Please run %s list %s first.
                        """, SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_RED));
        }
        if(gameID >= games.size()){
            throw new ResponseException(400, "Game may not exist, please use " + SET_TEXT_COLOR_BLUE + "list "
                    + SET_TEXT_COLOR_RED + "and verify that that is in-fact the game you desire");
        }
        return games.get(gameID);
    }


    public String observe(String ...params) throws ResponseException{
        if(params.length >= 1){
            int gameID = Integer.parseInt(params[0]);
            GameData game = checkGame(gameID);
            return String.format("Observing: %s%n%s", game.gameName(), BoardPrinter.print(game.game().getBoard()));
        }
        else{
            throw new ResponseException(400, "Expected: <ID>");
        }
    }


    public String logout() throws ResponseException{
        server.logout(authToken);
        return "transition ;; Good by";
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
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_LIGHT_GREY,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_LIGHT_GREY,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_LIGHT_GREY,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_LIGHT_GREY
                ,SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_LIGHT_GREY,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_LIGHT_GREY,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_LIGHT_GREY
                );
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
