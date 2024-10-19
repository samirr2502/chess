package dataaccess.gamedao;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{
  @Override
  public GameData getGame(String gameName) {
    for (GameData game: games){
      if(game.gameName().equals(gameName)){
        return game;
      }
    }
    return null;
  }

  @Override
  public GameData getGameByID(int gameID) {
    for (GameData game: games){
      if(game.gameID() ==gameID){
        return game;
      }
    }
    return null;
  }

  @Override
  public ArrayList<GameData> getGames() {
    return games;
  }

  @Override
  public void addGameData(GameData gameData) {
    games.add(gameData);
  }
  @Override
  public void updateGame(GameData gameData){
      deleteGameData(getGame(gameData.gameName()));
      addGameData(gameData);
  }
  @Override
  public void deleteGameData(GameData gameData) {
    games.remove(gameData);
  }

  @Override
  public void addPlayer(UserData user, GameData newGameData, ChessGame.TeamColor color) {

  }

  @Override
  public void deleteAllGames() {
    games.clear();
  }
}
