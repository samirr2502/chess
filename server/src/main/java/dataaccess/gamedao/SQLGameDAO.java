package dataaccess.gamedao;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
  @Override
  public GameData getGame(String gameName) throws DataAccessException, SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT json FROM gameData WHERE username=?";
      var ps = conn.prepareStatement(statement);
      ps.setString(1, gameName);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          String json = rs.getString("json");
          return new Gson().fromJson(json, GameData.class);
        }
        return null;
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }

  @Override
  public GameData getGameByID(int gameID) throws DataAccessException, SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT json FROM gameData WHERE id=?";
      var ps = conn.prepareStatement(statement);
      ps.setInt(1, gameID);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          String json = rs.getString("json");
          return new Gson().fromJson(json, GameData.class);
        }
        return null;
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }

  @Override
  public ArrayList<GameData> getGames() throws DataAccessException, SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT json FROM gameData";
      var ps = conn.prepareStatement(statement);
      try (var rs = ps.executeQuery()) {
        ArrayList<GameData> games = new ArrayList<>();
        while (rs.next()) {
          String json = rs.getString("json");
          games.add(new Gson().fromJson(json, GameData.class));
        }
        return games;
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }

  @Override
  public void addGameData(GameData gameData) throws DataAccessException, SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName,game,json) values (?,?,?,?,?)";
      var json = new Gson().toJson(gameData);
      var game = new Gson().toJson(gameData.game());
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1,gameData.whiteUsername());
        ps.setString(2,gameData.blackUsername());
        ps.setString(3,gameData.gameName());
        ps.setString(4,game);
        ps.setString(5,json);
        ps.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }

  @Override
  public void updateGame(GameData gameData) throws DataAccessException, SQLException {
    deleteGameData(gameData);
    addGameData(gameData);
  }

  @Override
  public void deleteGameData(GameData gameData) throws DataAccessException, SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "DELETE FROM gameData WHERE id=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setInt(1,gameData.gameID());
        ps.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
  }

  @Override
  public void deleteAllGames() throws DataAccessException, SQLException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "TRUNCATE gameData";
      try (var ps = conn.prepareStatement(statement)) {
        ps.executeUpdate();
      } catch (Exception e) {
        throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
      }
    }
  }
}
