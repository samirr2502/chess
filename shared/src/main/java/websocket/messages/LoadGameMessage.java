package websocket.messages;

import model.GameData;

public class LoadGameMessage implements MessageType {
  public String gameData;
  public LoadGameMessage(String gameData){
    this.gameData =gameData;
  }
}
