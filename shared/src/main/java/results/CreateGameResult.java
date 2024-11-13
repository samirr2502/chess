package results;

public class CreateGameResult extends Result {
  int gameID;
  public CreateGameResult(int gameID){
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }
}
