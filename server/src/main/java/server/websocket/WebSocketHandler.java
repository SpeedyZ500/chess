package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.UserData;
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

    private void handleConnect(Session session, String message) {
        ConnectCommand command = new Gson().fromJson(message, ConnectCommand.class);


    }

    private void handleLeve(Session session, String message){
        LeaveCommand command = new Gson().fromJson(message, LeaveCommand.class);

    }

    private void handleMakeMove(Session session, String message) throws InvalidMoveException, ResponseException{
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
        String authToken = command.getAuthToken();
        GameData gameData = gameService.getGame(command.getGameID());
        String username = userService.getUsername(authToken);
        ChessGame.TeamColor currentTurn = gameData.game().getTeamTurn();
        if( (currentTurn == ChessGame.TeamColor.WHITE && gameData.whiteUsername() == username)
            || (currentTurn == ChessGame.TeamColor.BLACK && gameData.blackUsername() == username)
        ){
            ChessGame game = gameData.game();
            game.makeMove(command.getMove());
            gameData = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    game
            );
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
