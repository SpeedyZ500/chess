package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final GameService gameService;
    private final UserService userService;
    private final ConnectionManager connectionManager = new ConnectionManager();

    public WebSocketHandler(GameService gameService, UserService userService){
        this.gameService = gameService;
        this.userService = userService;
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
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
        ConnectCommand command = new Gson().fromJson(message, ConnectCommand.class);
        String username = userService.getUsername(command.getAuthToken());
        int gameID = command.getGameID();
        GameData gameData = gameService.getGame(gameID);
        connectionManager.add(gameID, new Connection(session, username));
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

    private void handleLeve(Session session, String message){
        LeaveCommand command = new Gson().fromJson(message, LeaveCommand.class);

    }

    private void handleMakeMove(Session session, String message) throws InvalidMoveException, ResponseException{
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
        String authToken = command.getAuthToken();
        GameData gameData = gameService.getGame(command.getGameID());
        String username = userService.getUsername(authToken);
        ChessGame game = gameData.game();
        ChessGame.TeamColor currentTurn = game.getTeamTurn();

        if( (currentTurn == ChessGame.TeamColor.WHITE && username.equals(gameData.whiteUsername()) )
            || (currentTurn == ChessGame.TeamColor.BLACK && username.equals(gameData.blackUsername()))
        ){
            game.makeMove(command.getMove());
            gameData = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    game
            );
            gameService.updateGame(gameData);
        }
        else{
            throw new InvalidMoveException("Error: it is not your turn or you are not a player");
        }

    }
    private void handleResign(Session session, String message){
        ResignCommand command = new Gson().fromJson(message, ResignCommand.class);

    }

    private void sendError(Session session, String errorMessage) throws IOException {
        ErrorMessage error = new ErrorMessage(errorMessage);
        session.getRemote().sendString(new Gson().toJson(error,ErrorMessage.class));
    }
}
