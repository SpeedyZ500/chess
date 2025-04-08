package client.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notifier;
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

                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    if(notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                        notifier.notify(new Gson().fromJson(message, NotificationMessage.class));
                    }
                    else if(notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
                        notifier.warn(new Gson().fromJson(message, ErrorMessage.class));
                    }
                    else if(notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){

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
}
