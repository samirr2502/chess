package dataaccess.gamedao;

import dataaccess.DataAccessException;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
  @Override
  public GameData getGame(String gameName) throws DataAccessException, SQLException {
    return null;
  }

  @Override
  public GameData getGameByID(int gameID) throws DataAccessException, SQLException {
    return null;
  }

  @Override
  public ArrayList<GameData> getGames() throws DataAccessException, SQLException {
    return null;
  }

  @Override
  public void addGameData(GameData gameData) throws DataAccessException, SQLException {

  }

  @Override
  public void updateGame(GameData gameData) throws DataAccessException, SQLException {

  }

  @Override
  public void deleteGameData(GameData gameData) throws DataAccessException, SQLException {

  }

  @Override
  public void deleteAllGames() throws DataAccessException, SQLException {

  }
}
