package dataaccess.gamedao;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public interface GameDAO {
  ArrayList<GameData> games = new ArrayList<>();

  public GameData getGame(String gameName);
  public GameData getGameByID(int gameID);
  public ArrayList<GameData> getGames();
  public void addGameData(GameData gameData);
  public void updateGame(GameData gameData);
  public void deleteGameData(GameData gameData);
  public void addPlayer(UserData user, GameData gameData, ChessGame.TeamColor color);
  public void deleteAllGames();
}
