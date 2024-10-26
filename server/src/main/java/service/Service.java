package service;

import chess.ChessGame;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.results.*;

import java.util.ArrayList;
import java.util.UUID;

public class Service {


  public AuthData createAuthData(UserData userData, String authToken, MemoryAuthDAO memoryAuthDAO) {
    AuthData authData = memoryAuthDAO.getAuthDataByToken(authToken);
    if (authData == null) {
      return new AuthData(authToken, userData.username());
    }
    return null;
  }

  public AuthData getAuthData(AuthRequest authRequest, MemoryAuthDAO memoryAuthDAO) {
    return memoryAuthDAO.getAuthDataByToken(authRequest.authToken());
  }

  //User Level
  public LoginResult registerUser(UserData registerUserRequest, String authToken,
                                  MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) {
    UserData user = memoryUserDAO.getUser(registerUserRequest.username());
    if (user == null) {
      memoryUserDAO.addUser(registerUserRequest);
      AuthData newAuthData = createAuthData(registerUserRequest,authToken,memoryAuthDAO);
      memoryAuthDAO.addAuthData(newAuthData);
      return new LoginResult(newAuthData.username(), newAuthData.authToken());
    }
    return null;
  }

  public LoginResult loginUser(UserData loginRequest, String authToken, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) {
    UserData user = memoryUserDAO.getUser(loginRequest.username());
    if (user != null && loginRequest.password().equals(user.password())) {
      AuthData newAuthData = createAuthData(user, authToken, memoryAuthDAO);
      memoryAuthDAO.addAuthData(newAuthData);
      return new LoginResult(newAuthData.username(), newAuthData.authToken());
    } else {
      return null;
    }
  }

  public LogoutResult logoutUser(AuthRequest logoutRequest, MemoryAuthDAO memoryAuthDAO) {
    AuthData authData = getAuthData(logoutRequest, memoryAuthDAO);
    if (authData != null) {
      memoryAuthDAO.deleteAuthData(authData);
      return new LogoutResult();
    } else {
      return null;
    }
  }

  public GetGamesResult getGames(AuthRequest getGamesRequest, MemoryGameDAO memoryGameDAO, MemoryAuthDAO memoryAuthDAO) {
    AuthData authData = getAuthData(getGamesRequest, memoryAuthDAO);
    if (authData != null) {
      ArrayList<GameData> games = memoryGameDAO.getGames();
      return new GetGamesResult(games);
    } else {
      return null;
    }
  }

  public CreateGameResult createGame(AuthRequest authRequest, CreateGameRequest createGameRequest,
                                     MemoryGameDAO memoryGameDAO, MemoryAuthDAO memoryAuthDAO) {
    AuthData authData = getAuthData(authRequest, memoryAuthDAO);
    GameData gameData = memoryGameDAO.getGame(createGameRequest.gameName());
    if (authData != null && gameData == null) {
      ChessGame newGame = new ChessGame();
      GameData newGameData = new GameData(memoryGameDAO.getGames().size() + 1,
              null, null, createGameRequest.gameName(), newGame);
      memoryGameDAO.addGameData(newGameData);
      return new CreateGameResult(newGameData.gameID());
    }
    return null;
  }

  public JoinGameResult joinGame(AuthRequest authRequest, JoinGameRequest joinGameRequest, MemoryAuthDAO memoryAuthDAO, MemoryGameDAO memoryGameDAO) {
    AuthData authData = getAuthData(authRequest, memoryAuthDAO);
    GameData gameData = memoryGameDAO.getGameByID(joinGameRequest.gameID());
    if (gameData == null) {
      return new JoinGameResult(null, true);
    } else if (authData != null) {
      if (joinGameRequest.playerColor().equals("WHITE") && gameData.whiteUsername() == null) {
        memoryGameDAO.updateGame(new GameData(gameData.gameID(), authData.username(), gameData.blackUsername(),
                gameData.gameName(), gameData.game()));
        return new JoinGameResult(gameData, true);
      } else if (joinGameRequest.playerColor().equals("BLACK") && gameData.blackUsername() == null) {
        memoryGameDAO.updateGame(new GameData(gameData.gameID(), gameData.whiteUsername(), authData.username(),
                gameData.gameName(), gameData.game()));
        return new JoinGameResult(gameData, true);
      } else {
        return new JoinGameResult(gameData, false);
      }
    }
    return null;
  }

  public ClearResult clear(MemoryAuthDAO memoryAuthDAO, MemoryUserDAO memoryUserDAO, MemoryGameDAO memoryGameDAO) {
    memoryAuthDAO.deleteAllAuthData();
    memoryUserDAO.deleteAllUsers();
    memoryGameDAO.deleteAllGames();
    return new ClearResult();
  }

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }

}
