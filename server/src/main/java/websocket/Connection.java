package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String clientUserName;
    public Session session;

    public Connection(String clientUserName, Session session) {
        this.clientUserName = clientUserName;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}