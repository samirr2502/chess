package service.results;

public class GameResult extends Result {
  public int gameID;
  public String whiteUsername;
  public String blackUsername;
  public String gameName;
  public GameResult(int gameID, String whiteUsername, String blackUsername,
                     String gameName){
    this.gameID=gameID;
    this.whiteUsername =whiteUsername;
    this.blackUsername=blackUsername;
    this.gameName = gameName;
  }
}
