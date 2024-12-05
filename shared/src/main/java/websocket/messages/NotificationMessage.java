package websocket.messages;

public class NotificationMessage implements MessageType{
  public String message;
  public NotificationMessage(String message){
    this.message =message;
  }
}
