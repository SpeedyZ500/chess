package server.websocket;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import service.GameService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler {
    private final GameService gameService;
    private final UserService userService;

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
            }
        } catch (ResponseException | InvalidMoveException e) {
            sendError(session, e.getMessage());
        }
    }

    private void handleConnect(Session session, String message){
        ConnectCommand command = new Gson().fromJson(message, ConnectCommand.class);

    }

    private void handleLeve(Session session, String message){
        LeaveCommand command = new Gson().fromJson(message, LeaveCommand.class);

    }

    private void handleMakeMove(Session session, String message) throws InvalidMoveException{
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);


    }
    private void handleResign(Session session, String message){
        ResignCommand command = new Gson().fromJson(message, ResignCommand.class);

    }

    private void sendError(Session session, String errorMessage) throws IOException {
        ErrorMessage error = new ErrorMessage(errorMessage);
        session.getRemote().sendString(new Gson().toJson(error,ErrorMessage.class));
    }
}
