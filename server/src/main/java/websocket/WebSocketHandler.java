package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


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
    public void onMessage(Session session, String message) throws IOException, SQLException, DataAccessException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        GameData gameData = getGameData(command.getGameID());
        if(gameData== null) {
            var messageToMe = new ServerMessage(ServerMessage.ServerMessageType.ERROR,"Error Adding Game");
            connections.sendToMe(command.getGameID(),session, messageToMe);

        } else  if (authDAO.getAuthDataByToken(command.getAuthToken()) == null){
            var messageToMe = new ServerMessage(ServerMessage.ServerMessageType.ERROR,"Error Not authorized");
            connections.sendToMe(command.getGameID(),session, messageToMe);
        }
        else {
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, session, gameData);
                case MAKE_MOVE -> makeMove(command, session, gameData);
                case LEAVE -> leave(command, session);
                case RESIGN -> resign(command, session);
                case GET_GAME -> getGame(command.getGameID(), session);
            }
        }
    }
    private GameData getGame(Integer gameId, Session session) throws SQLException, DataAccessException, IOException {

        String jsonGame = new Gson().toJson(gameDAO.getGameByID(gameId));
        LoadGameMessage loadGameMessage = new LoadGameMessage(jsonGame);
        var messageToMe = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, loadGameMessage.gameData);
        connections.sendToMe(gameId,session, messageToMe);
        return null;
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
    private void makeMove(UserGameCommand command, Session session,GameData gameData) throws IOException, SQLException, DataAccessException, InvalidMoveException {

        if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE) ||gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)
//        ||gameData.game().isInStalemate(ChessGame.TeamColor.WHITE) || gameData.game().isInStalemate(ChessGame.TeamColor.BLACK)
            ) {
            var errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: game over");
            connections.sendToMe(command.getGameID(), session, errorMessage);
            return;
        } else if (gameData.game().getTeamTurn() == ChessGame.TeamColor.WHITE){
            if (!Objects.equals(authDAO.getAuthDataByToken(command.getAuthToken()).username(), gameData.whiteUsername())) {
                var errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: Not your turn");
                connections.sendToMe(command.getGameID(), session, errorMessage);
                return;
            }
        }else if (gameData.game().getTeamTurn() == ChessGame.TeamColor.BLACK){
            if (!Objects.equals(authDAO.getAuthDataByToken(command.getAuthToken()).username(), gameData.blackUsername())) {
                var errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: Not your turn");
                connections.sendToMe(command.getGameID(), session, errorMessage);
                return;
            }
        }
        if(command.getMove()!= null) {
            Collection<ChessMove> validMoves = gameData.game().validMoves(command.getMove().getStartPosition());
            if (validMoves == null || validMoves.isEmpty()) {
                var errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move");
                connections.sendToMe(command.getGameID(), session, errorMessage);
            } else {
                try {
                    System.out.println(new Gson().toJson(gameData.game().getBoard()));
                    gameData.game().makeMove(command.getMove());
                    System.out.println(new Gson().toJson(gameData.game().getBoard()));

                    String jsonGameData = new Gson().toJson(gameData);
                    System.out.println(jsonGameData);
                    LoadGameMessage loadGameMessage = new LoadGameMessage(jsonGameData);

                    var loadGameServerMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, loadGameMessage.gameData);
                    connections.sendToMe(command.getGameID(), session, loadGameServerMessage);
                    connections.broadcast(command.getGameID(), session, loadGameServerMessage);
                    gameDAO.updateGame(gameData);


                    NotificationMessage notificationMessage = new NotificationMessage("new message");
                    var notificationServerMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage.message);
                    connections.broadcast(command.getGameID(), session, notificationServerMessage);
                } catch (InvalidMoveException ex) {
                    var errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: invalid move");
                    connections.sendToMe(command.getGameID(), session, errorMessage);
                }
            }
        }
    }

    private void leave(UserGameCommand command, Session session) throws IOException, SQLException, DataAccessException {
        GameData gameData =gameDAO.getGameByID(command.getGameID());
        AuthData authData = authDAO.getAuthDataByToken(command.getAuthToken());
        if (Objects.equals(authData.username(), gameData.whiteUsername())) {
            GameData newGameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameDAO.updateGame(newGameData);
        } else if(Objects.equals(authData.username(), gameData.blackUsername())){
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            gameDAO.updateGame(newGameData);
        }
        connections.remove(command.getGameID(),session);
        var message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null);
        connections.broadcast(command.getGameID(),session, message);
    }
    private void resign(UserGameCommand command, Session session) throws IOException, SQLException, DataAccessException {
        GameData gameData =gameDAO.getGameByID(command.getGameID());
        var message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null);
        connections.broadcast(command.getGameID(), session, message);
    }
}