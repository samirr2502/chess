package dataaccess.gamedao;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public interface GameDAO {
  ArrayList<GameData> games = new ArrayList<>();

  public GameData getGame(String gameName);
  public ArrayList<GameData> getGames();
  public void addGameData(GameData gameData);
  public void deleteGameData(GameData gameData);
  public void addPlayer(UserData user, GameData gameData);
  public void deleteAllGames();
}
