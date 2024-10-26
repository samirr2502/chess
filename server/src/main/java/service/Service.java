package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.userdao.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.results.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Service{
  private final AuthDAO authDAO;
  private final GameDAO gameDAO;
  private final UserDAO userDAO;

  public Service(DataAccess dataAccess){
    this.authDAO = dataAccess.getAuthDAO();
    this.gameDAO = dataAccess.getGameDAO();
    this.userDAO = dataAccess.getUserDAO();
  }
  public AuthData createAuthData(UserData userData, String authToken) throws SQLException, DataAccessException {
    AuthData authData = this.authDAO.getAuthDataByToken(authToken);
    if (authData == null) {
      return new AuthData(authToken, userData.username());
    }
    return null;
  }

  public AuthData getAuthData(AuthRequest authRequest) throws SQLException, DataAccessException {
    return this.authDAO.getAuthDataByToken(authRequest.authToken());
  }

  //User Level
  public LoginResult registerUser(UserData registerUserRequest, String authToken) throws SQLException, DataAccessException {
    UserData user = this.userDAO.getUser(registerUserRequest.username());
    if (user == null) {

      this.userDAO.addUser(registerUserRequest);
      AuthData newAuthData = createAuthData(registerUserRequest, authToken);
      this.authDAO.addAuthData(newAuthData);
      return new LoginResult(newAuthData.username(), newAuthData.authToken());
    }
    return null;
  }

  public LoginResult loginUser(UserData loginRequest, String authToken) throws SQLException, DataAccessException {
    UserData user = this.userDAO.getUser(loginRequest.username());

    if (user != null && BCrypt.checkpw(loginRequest.password(), user.password())){
      AuthData newAuthData = createAuthData(user, authToken);
      this.authDAO.addAuthData(newAuthData);
      return new LoginResult(newAuthData.username(), newAuthData.authToken());
    } else {
      return null;
    }
  }

  public LogoutResult logoutUser(AuthRequest logoutRequest) throws SQLException, DataAccessException {
    AuthData authData = getAuthData(logoutRequest);
    if (authData != null) {
      this.authDAO.deleteAuthData(authData);
      return new LogoutResult();
    } else {
      return null;
    }
  }

  public GetGamesResult getGames(AuthRequest getGamesRequest) throws SQLException, DataAccessException {
    AuthData authData = getAuthData(getGamesRequest);
    if (authData != null) {
      ArrayList<GameData> games = this.gameDAO.getGames();
      return new GetGamesResult(games);
    } else {
      return null;
    }
  }

  public CreateGameResult createGame(AuthRequest authRequest, CreateGameRequest createGameRequest) throws SQLException, DataAccessException {
    AuthData authData = getAuthData(authRequest);
    GameData gameData = this.gameDAO.getGameByName(createGameRequest.gameName());
    if (authData != null && gameData == null) {
      ChessGame newGame = new ChessGame();
      GameData newGameData = new GameData(this.gameDAO.getGames().size() + 1,
              null, null, createGameRequest.gameName(), newGame);
      this.gameDAO.addGameData(newGameData);
      return new CreateGameResult(newGameData.gameID());
    }
    return null;
  }

  public JoinGameResult joinGame(AuthRequest authRequest, JoinGameRequest joinGameRequest) throws SQLException, DataAccessException {
    AuthData authData = getAuthData(authRequest);
    GameData gameData = this.gameDAO.getGameByID(joinGameRequest.gameID());
    if (gameData == null) {
      return new JoinGameResult(null, true);
    } else if (authData != null) {
      if (joinGameRequest.playerColor().equals("WHITE") && gameData.whiteUsername() == null) {
        this.gameDAO.updateGame(new GameData(gameData.gameID(), authData.username(), gameData.blackUsername(),
                gameData.gameName(), gameData.game()));
        return new JoinGameResult(gameData, true);
      } else if (joinGameRequest.playerColor().equals("BLACK") && gameData.blackUsername() == null) {
        this.gameDAO.updateGame(new GameData(gameData.gameID(), gameData.whiteUsername(), authData.username(),
                gameData.gameName(), gameData.game()));
        return new JoinGameResult(gameData, true);
      } else {
        return new JoinGameResult(gameData, false);
      }
    }
    return null;
  }

  public ClearResult clear() throws SQLException, DataAccessException {
    this.authDAO.deleteAllAuthData();
    this.userDAO.deleteAllUsers();
    this.gameDAO.deleteAllGames();
    return new ClearResult();
  }

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }

}
