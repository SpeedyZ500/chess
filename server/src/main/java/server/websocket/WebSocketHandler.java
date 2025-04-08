package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ResponseException;
import gson.GsonConfig;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.List;

@WebSocket
public class WebSocketHandler {
    private final GameService gameService;
    private final UserService userService;
    private final ConnectionManager connectionManager = new ConnectionManager();
    private static final Gson GSON = GsonConfig.createGson();;

    public WebSocketHandler(GameService gameService, UserService userService){
        this.gameService = gameService;
        this.userService = userService;
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = GSON.fromJson(message, UserGameCommand.class);
        try{
            if(!userService.verifyToken(command.getAuthToken())){
                sendError(session, "Error: Invalid Token");
                return;
            }
            if(gameService.getGame(command.getGameID()) == null){
                sendError(session,"Error: Game does not exist");
                return;
            }
            switch(command.getCommandType()){
                case CONNECT -> handleConnect(session, message);
                case LEAVE -> handleLeve(session, message);
                case MAKE_MOVE -> handleMakeMove(session, message);
                case RESIGN -> handleResign(session, message);
                default -> sendError(session, "Error: Missing command type");
            }
        } catch (ResponseException | InvalidMoveException e) {
            sendError(session, e.getMessage());
        }
    }

    private void handleConnect(Session session, String message) throws ResponseException, IOException {
        ConnectCommand command = GSON.fromJson(message, ConnectCommand.class);
        String username = userService.getUsername(command.getAuthToken());
        int gameID = command.getGameID();
        GameData gameData = gameService.getGame(gameID);
        connectionManager.add(gameID, new Connection(session, username));
        LoadGameMessage loadGame = new LoadGameMessage(gameData.game());
        session.getRemote().sendString(GSON.toJson(loadGame));
        String as = "an Observer";
        if(username.equals(gameData.whiteUsername())){
            as = "White Player";
        }
        else if(username.equals(gameData.blackUsername())){
            as = "Black Player";
        }
        NotificationMessage notification = new NotificationMessage(String.format("%s joined the game as %s"
                , username, as));
        connectionManager.broadcast(gameID,username, notification);
    }

    private void handleLeve(Session session, String message) throws ResponseException, IOException {
        LeaveCommand command = GSON.fromJson(message, LeaveCommand.class);
        int gameID = command.getGameID();
        String username = userService.getUsername(command.getAuthToken());
        GameData gameData = gameService.getGame(gameID);
        gameData = new GameData(
                gameID,
                username.equals(gameData.whiteUsername()) ? null : gameData.whiteUsername(),
                username.equals(gameData.blackUsername()) ? null : gameData.blackUsername(),
                gameData.gameName(),
                gameData.game()
                );
        gameService.updateGame(gameData);

        connectionManager.remove(gameID, session, username);
        NotificationMessage notification = new NotificationMessage(String.format("%s has left the game", username));
        connectionManager.broadcast(gameID,username, notification);

    }

    private void handleMakeMove(Session session, String message) throws InvalidMoveException, ResponseException, IOException {
        MakeMoveCommand command = GSON.fromJson(message, MakeMoveCommand.class);
        String authToken = command.getAuthToken();
        GameData gameData = gameService.getGame(command.getGameID());
        String username = userService.getUsername(authToken);
        ChessGame game = gameData.game();
        ChessGame.TeamColor currentTurn = game.getTeamTurn();
        if(currentTurn == null){
            sendError(session, "Error: cannot make move, the game is over");
        }
        else if( (currentTurn == ChessGame.TeamColor.WHITE && username.equals(gameData.whiteUsername()) )
            || (currentTurn == ChessGame.TeamColor.BLACK && username.equals(gameData.blackUsername()))
        ){
            ChessMove move = command.getMove();
            game.makeMove(move);
            gameData = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    game
            );
            gameService.updateGame(gameData);
            LoadGameMessage loadGame = new LoadGameMessage(gameData.game());
            connectionManager.broadcast(gameData.gameID(), null, loadGame);
            NotificationMessage moveNotification = new NotificationMessage(
                    String.format("%s, moved from %s, to %s, it is now %s's turn",
                            username,
                            move.getStartPosition().prettyOutput(),
                            move.getEndPosition().prettyOutput(),
                            game.getTeamTurn()
                    )
            );
            connectionManager.broadcast(gameData.gameID(), username, moveNotification);
            if(game.isInCheckmate(ChessGame.TeamColor.WHITE)){
                NotificationMessage checkMateNotification = new NotificationMessage(
                        String.format("""
                                %s (The White Player) is in CheckMate, and has lost the game.
                                %s (The Black Player) has won the game!
                                """, gameData.whiteUsername(), gameData.blackUsername())
                );
                connectionManager.broadcast(gameData.gameID(), null, checkMateNotification);
            }
            else if(game.isInCheckmate(ChessGame.TeamColor.BLACK)){
                NotificationMessage checkMateNotification = new NotificationMessage(
                        String.format("""
                                %s (The Black Player) is in CheckMate, and has lost the game.
                                %s (The White Player) has won the game!
                                """, gameData.blackUsername(), gameData.whiteUsername())
                );
                connectionManager.broadcast(gameData.gameID(), null, checkMateNotification);
            }
            else if(game.isInCheck(ChessGame.TeamColor.WHITE)){
                NotificationMessage checkNotification = new NotificationMessage(
                        String.format("%s (The White Player) is in check", gameData.whiteUsername())
                );
                connectionManager.broadcast(gameData.gameID(), null, checkNotification);

            }
            else if(game.isInCheck(ChessGame.TeamColor.BLACK)){
                NotificationMessage checkNotification = new NotificationMessage(
                        String.format("%s (The Black Player) is in check", gameData.blackUsername())
                );
                connectionManager.broadcast(gameData.gameID(), null, checkNotification);
            }
            else if(game.isInStalemate(game.getTeamTurn())){
                NotificationMessage stalemateNotification = new NotificationMessage(
                        "Stalemate, there are no more valid moves, and no one is in check, Game Ends in a draw."
                );
                connectionManager.broadcast(gameData.gameID(), null, stalemateNotification);
            }
        }
        else{
            throw new InvalidMoveException("Error: it is not your turn or you are not a player");
        }
    }
    private void handleResign(Session session, String message){
        ResignCommand command = GSON.fromJson(message, ResignCommand.class);

    }

    private void sendError(Session session, String errorMessage) throws IOException {
        ErrorMessage error = new ErrorMessage(errorMessage);
        session.getRemote().sendString(GSON.toJson(error,ErrorMessage.class));
    }
}
