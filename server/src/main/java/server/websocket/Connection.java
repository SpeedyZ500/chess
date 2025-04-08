package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;


public class Connection {
    private final Session session;
    private final String username;

    public Connection(Session session, String username){
        this.session = session;
        this.username = username;
    }

    public Session getSession(){
        return session;
    }

    public String getUsername(){
        return username;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
