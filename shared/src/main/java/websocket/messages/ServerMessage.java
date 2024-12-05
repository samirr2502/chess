package websocket.messages;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    String game;
    String message;
    String errorMessage;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }
    public ServerMessage(ServerMessageType type, String messageType) {
        this.serverMessageType = type;
        switch (type){
            case NOTIFICATION -> this.message = messageType;
            case LOAD_GAME -> this.game = messageType;
            case ERROR ->  this.errorMessage = messageType;
        }
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
