package dataaccess.gamedao;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
  @Override
  public GameData getGameByName(String gameName) {
    for (GameData game : GAMES) {
      if (game.gameName().equals(gameName)) {
        return game;
      }
    }
    return null;
  }

  @Override
  public GameData getGameByID(int gameID)   {
    for (GameData game : GAMES) {
      if (game.gameID() == gameID) {
        return game;
      }
    }
    return null;
  }

  @Override
  public ArrayList<GameData> getGames() {
    return GAMES;
  }

  @Override
  public void addGameData(GameData gameData) {
    GAMES.add(gameData);
  }

  @Override
  public void updateGame(GameData gameData) {
    deleteGameData(getGameByName(gameData.gameName()));
    addGameData(gameData);
  }

  @Override
  public void deleteGameData(GameData gameData) {
    GAMES.remove(gameData);
  }

  @Override
  public void deleteAllGames() {
    GAMES.clear();
  }
}
