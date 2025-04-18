package server.websocket;

import com.google.gson.Gson;
import gson.GsonConfig;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class ConnectionManager {
    private final Map<Integer, List<Connection>> sessionsByGame = new ConcurrentHashMap<>();
    private static final Gson GSON = GsonConfig.createGson();;
    public ConnectionManager(){}
    public void add(int gameID, Connection connection){
        if(!sessionsByGame.containsKey(gameID)){
            sessionsByGame.put(gameID, new CopyOnWriteArrayList<>());
        }
        sessionsByGame.get(gameID).add(connection);
    }

    public void remove(int gameID, Session session, String username) {
        List<Connection> connections = sessionsByGame.get(gameID);
        if (connections != null){
            for(Connection connection : connections){
                if(connection.getSession().equals(session) && connection.getUsername().equals(username)){
                    connections.remove(connection);
                    break;
                }
            }
            if(connections.isEmpty()){
                sessionsByGame.remove(gameID);
            }
        }
    }


    public void broadcast(int gameID, String excludeUsername, ServerMessage message) throws IOException {
        List<Connection> connections = sessionsByGame.get(gameID);
        List<Connection> removeList = new ArrayList<>();

        for (var c : connections) {
            String username = c.getUsername();
            Session session = c.getSession();

            if (session.isOpen()) {
                if (!username.equals(excludeUsername)) {
                    c.send(GSON.toJson(message));
                }
            } else {
                removeList.add(c);
            }
        }
        for(var c : removeList){
            connections.remove(c);
        }
    }


}
