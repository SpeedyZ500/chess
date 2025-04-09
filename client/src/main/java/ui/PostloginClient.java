package ui;

import chess.ChessGame;
import client.websocket.NotificationHandler;
import exception.ResponseException;
import model.GameData;
import client.ServerFacade;

import java.util.*;

import static ui.EscapeSequences.*;

public class PostloginClient implements Client{
    private final String serverUrl;
    private final ServerFacade server;
    private final NotificationHandler notifier;
    private final String authToken;
    private final String username;
    List<GameData> games;
    public PostloginClient(String serverUrl, NotificationHandler notifier, String username, String authToken){
        this.serverUrl = serverUrl;
        this.notifier = notifier;
        this.server = new ServerFacade(serverUrl);
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

    @Override
    //does nothing here;
    public String loadGame(ChessGame game) {
        return "";

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
        GameData[] gamesArray = server.listGames(authToken);
        games.addAll(Arrays.stream(gamesArray).toList());
        return "Games:\n" + printListOfGames();
    }

    private String printListOfGames(){
        StringBuilder output = new StringBuilder();
        output.append("+--------+-------------+-----------------+-----------------+\n");
        output.append(String.format("| %-6s | %-11s | %-15s | %-15s |%n",
                 "ID", "Game Name", "White User", "Black User" ));
        for(int i = 0; i < games.size(); i++){
            GameData game = games.get(i);
            String gameName = game.gameName() == null ? "NULL" : game.gameName();
            String whiteUsername = game.whiteUsername() == null ? "none" : game.whiteUsername();
            String blackUsername = game.blackUsername() == null ? "NULL" : game.blackUsername();

            output.append(String.format("| %s%-6d %s| %-11s | %s%-15s %s| %s%-15s %s|%n",
                    SET_TEXT_COLOR_YELLOW,
                    i+1,
                    SET_TEXT_COLOR_GREEN,
                    gameName,
                    SET_TEXT_COLOR_WHITE,
                    whiteUsername,
                    SET_TEXT_COLOR_GREEN,
                    SET_TEXT_COLOR_BLUE,
                    blackUsername,
                    SET_TEXT_COLOR_GREEN)
            );
            output.append("+--------+-------------+-----------------+-----------------+\n");

        }
        output.append(SET_TEXT_COLOR_GREEN);
        return output.toString();
    }

    public String join(String ...params) throws ResponseException{
        if (params.length >= 2) {
            GameData game = checkGame(params[0]);
            Set<String> validColors = Set.of("WHITE", "BLACK");
            if(!validColors.contains(params[1].trim().toUpperCase())){
                throw new ResponseException(400, "Expected: [WHITE|BLACK]");
            }
            if("WHITE".equalsIgnoreCase(params[1].trim()) && !username.equals(game.whiteUsername()) ||
                    "BLACK".equalsIgnoreCase(params[1].trim()) && !username.equals(game.blackUsername())){
                server.joinGame(params[1], game.gameID(), authToken);
                return String.format("transition ;; %d ;; Joining the Game", game.gameID());
            }
            else{
                return BoardPrinter.print(
                        ChessGame.TeamColor.valueOf(params[1].trim().toUpperCase()),
                        game.game().getBoard()
                );

            }

        }
        else {
            throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");
        }
    }

    private GameData checkGame(String stringGameID) throws ResponseException{
        try {
            int gameID = Integer.parseInt(stringGameID) - 1;
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
            if(gameID < 0){
                throw new ResponseException(400, "<ID> must be Greater than 1");
            }
            return games.get(gameID);
        }
        catch (NumberFormatException e){
            throw new ResponseException(400, "<ID> must be a number");
        }
    }


    public String observe(String ...params) throws ResponseException{
        if(params.length >= 1){
            GameData game = checkGame(params[0]);
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
    public Client transition(String token) throws ResponseException {
        int gameIndex = Integer.parseInt(token);
        GameData game = games.get(gameIndex);
        return new GameplayClient(serverUrl, notifier, username, authToken, game);
    }

    @Override
    public Client transition() {
        return new PreloginClient(serverUrl, notifier);
    }

    @Override
    public String terminalState(){
        return "[LOGGED_IN]";
    }
}
