package dataaccess.gamedao;

import model.GameData;
import model.UserData;

public class MemoryGameDAO implements GameDAO{
  @Override
  public GameData getGame(GameData gameData) {
    return null;
  }

  @Override
  public void addGameData(GameData gameData) {

  }

  @Override
  public void deleteGameData(GameData gameData) {

  }

  @Override
  public void addPlayer(UserData user, GameData gameData) {

  }

  @Override
  public void deleteAllGames() {
    games.clear();
  }
}
