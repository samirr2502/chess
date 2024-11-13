package results;

import model.GameData;

import java.util.ArrayList;

public class GetGamesResult extends Result {
  public final ArrayList<GameResult> games = new ArrayList<>();
  public GetGamesResult(ArrayList<GameData> gameDataList) {
    setGames(gameDataList);
  }

  public void setGames(ArrayList<GameData> gameDataList){
    for (GameData gameData:gameDataList){
      games.add(new GameResult(gameData.gameID(),gameData.whiteUsername(),
              gameData.blackUsername(), gameData.gameName()));
    }
  }

}

