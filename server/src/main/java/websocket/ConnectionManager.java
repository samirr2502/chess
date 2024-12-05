package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String rootClientUserName, Session session) {
        var connection = new Connection(rootClientUserName, session);
        connections.put(rootClientUserName, connection);
    }

    public void remove(String clientUserName) {
        connections.remove(clientUserName);
    }

    public void broadcast(String excludeRootUserName, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.clientUserName.equals(excludeRootUserName)) {
                    c.send(message.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.clientUserName);
        }
    }
}
