package database.gamedao;

import database.DataAccessException;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GameDAO {
  ArrayList<GameData> GAMES = new ArrayList<>();

  GameData getGameByName(String gameName) throws DataAccessException, SQLException;
  GameData getGameByID(int gameID) throws DataAccessException, SQLException;
  ArrayList<GameData> getGames() throws DataAccessException, SQLException;
  void addGameData(GameData gameData) throws DataAccessException, SQLException;
  void updateGame(GameData gameData) throws DataAccessException, SQLException;
  void deleteGameData(GameData gameData) throws DataAccessException, SQLException;
  void deleteAllGames() throws DataAccessException, SQLException;
}
