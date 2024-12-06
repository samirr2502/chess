package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.GameData;
import ui.DrawnBoard;
import ui.Repl;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {
  Session session;



  public WebSocketFacade(String url) throws Exception {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/ws");

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
          if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            Repl.currentGameData = new Gson().fromJson(serverMessage.getGame(), GameData.class);
            drawBoard();
            System.out.println("it's "+ Repl.currentGameData.game().getTeamTurn() + " turn");


          }
          printNotification(serverMessage);
          Repl.printPrompt();
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new Exception(ex.getMessage());
    }
  }

  //Endpoint requires this method, but you don't have to do anything
  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
  }


  public void printNotification(ServerMessage serverMessage){
    switch (serverMessage.getServerMessageType()){
      case NOTIFICATION -> System.out.println(serverMessage.getMessage());
      case LOAD_GAME -> System.out.println();
      case ERROR -> System.out.println(serverMessage.getErrorMessage());
    }
  }

  public void connectToGame(String authToken, int gameId ) throws Exception {
    try {
      var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken, gameId, null, Repl.lastJoinedGameColor);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }
  public void makeMove(String authToken, int gameId,ChessMove move ) throws Exception {
    try {
      var command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameId, move, Repl.lastJoinedGameColor);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException ex) {
      throw new Exception(ex.getMessage());
    }
  }
  public void getGame(String authToken, int gameId) throws Exception {
    try {
      var command = new UserGameCommand(UserGameCommand.CommandType.GET_GAME, authToken, gameId, null, Repl.lastJoinedGameColor);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void leaveGame(String authToken, int gameId ) throws Exception {
    try {
      if (this.session.isOpen()) {
        var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameId, null, Repl.lastJoinedGameColor);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
        this.session.close();
      }
    } catch (IOException ex) {
      throw new Exception(ex.getMessage());
    }
  }
  public void resign(String authToken, int gameId ) throws Exception {
    try {
      var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN,authToken, gameId, null, Repl.lastJoinedGameColor);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException ex) {
      throw new Exception(ex.getMessage());
    }
  }
  public void drawBoard(){
    System.out.println();
    if (Repl.lastJoinedGameColor == null || Repl.lastJoinedGameColor== ChessGame.TeamColor.WHITE) {
      DrawnBoard.run(ChessGame.TeamColor.WHITE, Repl.currentGameData.game().getBoard(), Repl.validMoves);
    } else{
      DrawnBoard.run(ChessGame.TeamColor.BLACK, Repl.currentGameData.game().getBoard(), Repl.validMoves);
    }
  }

}

