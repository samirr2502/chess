package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(),session);
            case MAKE_MOVE-> makeMove(command.getAuthToken());
            case LEAVE -> leave(command.getAuthToken());
            case RESIGN -> resign(command.getAuthToken());
        }
    }

    private void connect(String clientUserName, Session session) throws IOException {
        connections.add(clientUserName, session);
        //var message = String.format("%s is in the shop", visitorName);
        var message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(clientUserName, message);
    }
    private void makeMove(String commandMove) throws IOException {
        connections.remove(commandMove);
        //var message = String.format("%s left the shop", commandMove);
        var message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(commandMove, message);
    }

    private void leave(String clientUserName) throws IOException {
        connections.remove(clientUserName);
//        var message = String.format("%s left the shop", visitorName);
        var message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(clientUserName, message);
    }
    private void resign(String clientUserName) throws IOException {
        connections.remove(clientUserName);
        //var message = String.format("%s left the shop", visitorName);
        var message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(clientUserName, message);
    }

    public void makeNoise(String petName, String sound) throws Exception {
        try {
            var message = String.format("%s says %s", petName, sound);
            //var notification = new Notification(Notification.Type.NOISE, message);
            //connections.broadcast("", notification);
        } catch (Exception ex) {
           // throw new ResponseException(500, ex.getMessage());
        }
    }
}