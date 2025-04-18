package client.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import gson.GsonConfig;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notifier;
    private static final Gson GSON = GsonConfig.createGson();

    public WebSocketFacade(String url, NotificationHandler notifier) throws ResponseException{
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notifier = notifier;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = GSON.fromJson(message, ServerMessage.class);
                    if(notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                        notifier.notify(GSON.fromJson(message, NotificationMessage.class));
                    }
                    else if(notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
                        notifier.warn(GSON.fromJson(message, ErrorMessage.class));
                    }
                    else if(notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                        notifier.loadGame(GSON.fromJson(message, LoadGameMessage.class));
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void connectToGame(String authToken, int gameID) throws ResponseException{
        ConnectCommand command = new ConnectCommand(authToken, gameID);
        sendMessage(GSON.toJson(command));
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException{
        LeaveCommand command = new LeaveCommand(authToken, gameID);
        sendMessage(GSON.toJson(command));
    }
    private void sendMessage(String message) throws ResponseException {
        try{
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException{
        MakeMoveCommand command = new MakeMoveCommand(authToken, gameID, move);
        sendMessage(GSON.toJson(command));
    }

    public void resign(String authToken, int gameID) throws ResponseException{
        ResignCommand command = new ResignCommand(authToken, gameID);
        sendMessage(GSON.toJson(command));
    }

}
