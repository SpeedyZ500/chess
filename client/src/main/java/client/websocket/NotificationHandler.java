package client.websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notify(NotificationMessage notification);
    void warn(ErrorMessage error);


}
