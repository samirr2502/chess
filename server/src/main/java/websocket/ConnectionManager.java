package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();
    public void add(Integer gameID, Session session) {
        var connection = new Connection(gameID, session);
        ArrayList<Connection> connectionsList = new ArrayList<>();
        connections.putIfAbsent(gameID, connectionsList);
        connections.get(gameID).add(connection);
    }

    public void remove(Integer gameId, Session session) {
        ArrayList<Connection> connectionsList =  connections.get(gameId);
        for (Connection conn: connectionsList){
            if (conn.session == session){
                connections.get(gameId).remove(conn);
            }
        }
    }

    public void sendToMe(Integer gameId, Session session, ServerMessage message) throws IOException {
        String jsonMessage = new Gson().toJson(message);
        session.getRemote().sendString(jsonMessage);
    }

    public void broadcast(Integer gameId, Session session, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        String jsonMessage = new Gson().toJson(message);
        for (var c : connections.get(gameId)) {
            if (c.session.isOpen()) {
                if (!c.session.equals(session)) {
                    c.send(jsonMessage);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.get(gameId).remove(c);
        }
    }
}
