package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public Integer gameID;
    public Session session;

    public Connection(Integer gameId, Session session) {
        this.gameID = gameId;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        this.session.getRemote().sendString(msg);
    }
}