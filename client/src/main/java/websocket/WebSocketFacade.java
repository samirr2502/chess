package websocket;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import javax.management.Notification;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

  Session session;
//  NotificationHandler notificationHandler;


  public WebSocketFacade(String url) throws Exception {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/ws");
     // this.notificationHandler = notificationHandler;

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
          printNotification(serverMessage);
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
      case NOTIFICATION -> System.out.println("new notification");
      case LOAD_GAME -> System.out.println("joined");
      case ERROR -> System.out.println("oops Error");

    }

  }
  public void connectToGame(String authToken, int gameId ) throws Exception {
    try {
      var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken, gameId, null);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
    }
  }
  public void makeMove(String authToken, int gameId ) throws Exception {
    try {
      var command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameId, );
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void leaveGame(String authToken, int gameId ) throws Exception {
    try {
      if (this.session.isOpen()) {
        var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameId,null);
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
        this.session.close();
      }
    } catch (IOException ex) {
      throw new Exception(ex.getMessage());
    }
  }
  public void resign(String authToken, int gameId ) throws Exception {
    //try {
      //var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN,authToken, gameId );
      //this.session.getBasicRemote().sendText(new Gson().toJson(command));
      //this.session.close();
//    } catch (IOException ex) {
//      throw new Exception(ex.getMessage());
//    }
  }
}

