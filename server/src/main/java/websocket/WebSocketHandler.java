package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SQLDataAccess;
import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.authdao.SQLAuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.gamedao.SQLGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.SQLUserDAO;
import dataaccess.userdao.UserDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


import java.io.IOException;
import java.sql.SQLException;


@WebSocket
public class WebSocketHandler {
    static DataAccess sqlDataAccess;

    static DataAccess memoryDataAccess;

    static {

        try {
            sqlDataAccess = new SQLDataAccess(new SQLAuthDAO(), new SQLGameDAO(), new SQLUserDAO());

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        memoryDataAccess = new MemoryDataAccess(new MemoryAuthDAO(),new MemoryGameDAO(), new MemoryUserDAO());

        //Change the database here:
    }
    private final GameDAO gameDAO = sqlDataAccess.getGameDAO();
    private final UserDAO userDAO = sqlDataAccess.getUserDAO();
    private final AuthDAO authDAO = sqlDataAccess.getAuthDAO();
    private static final Gson JSON = new Gson();

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, SQLException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        GameData gameData = getGameData(command.getGameID());
        if(gameData== null) {
            var messageToMe = new ServerMessage(ServerMessage.ServerMessageType.ERROR,"Error Adding Game");
            connections.sendToMe(command.getGameID(),session, messageToMe);

        } else  if (authDAO.getAuthDataByToken(command.getAuthToken()) == null){
            var messageToMe = new ServerMessage(ServerMessage.ServerMessageType.ERROR,"Error Not authorized Game");
            connections.sendToMe(command.getGameID(),session, messageToMe);
        }
        else {
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, session, gameData);
                case MAKE_MOVE -> makeMove(command, session, gameData);
                case LEAVE -> leave(command.getGameID(), session);
                case RESIGN -> resign(command.getGameID(), session);
            }
        }
    }
    //Data Access methods:
    private GameData getGameData(Integer gameId) throws SQLException, DataAccessException {
        return gameDAO.getGameByID(gameId);
    }

    //Continue
    private void connect(UserGameCommand  command, Session session, GameData gameData) throws IOException, SQLException, DataAccessException {

        connections.add(command.getGameID(), session);

        String jsonGameData = new Gson().toJson(gameData);
        LoadGameMessage loadGameMessage = new LoadGameMessage(jsonGameData);
        NotificationMessage notificationMessage = new NotificationMessage("new message");

        var messageToMe = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, loadGameMessage.gameData);
        connections.sendToMe(command.getGameID(),session, messageToMe);

        var messageToAll = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage.message);
        connections.broadcast(command.getGameID(), session, messageToAll);

    }
    private void makeMove(UserGameCommand command, Session session,GameData gameData) throws IOException {
        String jsonGameData = new Gson().toJson(gameData);

        LoadGameMessage loadGameMessage = new LoadGameMessage(jsonGameData);
        var loadGameServerMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, loadGameMessage.gameData);
        connections.sendToMe(command.getGameID(),session, loadGameServerMessage);

        NotificationMessage notificationMessage = new NotificationMessage("new message");
        var notificationServerMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage.message);
        connections.broadcast(command.getGameID(),session, notificationServerMessage);
    }

    private void leave(Integer gameId, Session session) throws IOException {
        connections.remove(gameId,session );
//        var message = String.format("%s left the shop", visitorName);
        var message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null);
        connections.broadcast(gameId,session, message);
    }
    private void resign(Integer gameId, Session session) throws IOException {
        connections.remove(gameId, session);
        //var message = String.format("%s left the shop", visitorName);
        var message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null);
        connections.broadcast(gameId, session, message);
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