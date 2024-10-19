package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.AuthRequest;
import server.CreateGameRequest;
import spark.Response;

import java.util.ArrayList;
import java.util.UUID;

public class Service {

//User Level
  public Result registerUser(Response res, UserData newUser,
                             MemoryUserDAO memoryUserDAO,
                             MemoryAuthDAO memoryAuthDAO) throws DataAccessException {
    try{
      UserData user = memoryUserDAO.getUser(newUser.username());

      if (newUser.password() == null ||
              newUser.username() ==null ||
              newUser.email() ==null){
        res.status(400);
        return new ErrorResult("Error: bad request");
      }
    else if(user== null){
      //Add User
      memoryUserDAO.addUser(newUser);

      //Create and Add auth Data
      AuthData authData = new AuthData(generateToken(),newUser.username());
      memoryAuthDAO.addAuthData(authData);

      return new LoginResult(authData.username(),authData.authToken());
    }
    else {
      res.status(403);
      return new ErrorResult("Error: already taken");
    }
    } catch (DataAccessException dx){
      res.status(400);
      return new ErrorResult("Error: bad request");
    }
  }
  public Result loginUser(Response res, UserData newUser,
                          MemoryUserDAO memoryUserDAO,
                          MemoryAuthDAO memoryAuthDAO) throws DataAccessException {
    try{
      UserData user = memoryUserDAO.getUser(newUser.username());
      if(user!= null && newUser.password().equals(user.password())){
        AuthData authData = new AuthData(generateToken(), user.username());
        memoryAuthDAO.addAuthData(authData);
        return new LoginResult(authData.username(),authData.authToken());
      } else {
        res.status(401);
        return new ErrorResult("Error: unauthorized");
      }} catch (DataAccessException dx){
      res.status(400);
      return new ErrorResult("Error: bad request");
    }
  }

  public Result logoutUser(Response res, AuthRequest logoutRequest, MemoryAuthDAO memoryAuthDAO) {
      AuthData authData = memoryAuthDAO.getAuthData(logoutRequest.authToken());
      if (authData != null) {
        memoryAuthDAO.deleteAuthData(authData);
        return null;
      }else {
        res.status(401);
        return new ErrorResult("Error: unauthorized");
      }
  }
  public Result getGames(Response res, AuthRequest getGamesRequest,
                         MemoryGameDAO memoryGameDAO, MemoryAuthDAO memoryAuthDAO ){
    AuthData authData = memoryAuthDAO.getAuthData(getGamesRequest.authToken());
    if (authData != null){
      ArrayList<GameData> games=memoryGameDAO.getGames();
      return new GetGamesResult(games);
    }
    res.status(401);
    return new ErrorResult("Error: unauthorized");
  }
  public Result createGame(Response res,CreateGameRequest createGameRequest, String authToken,
                           MemoryGameDAO memoryGameDAO, MemoryAuthDAO memoryAuthDAO){
    AuthData authData = memoryAuthDAO.getAuthData(authToken);
    GameData gameData = memoryGameDAO.getGame(createGameRequest.gameName());
    if (authData != null && gameData == null) {
      ChessGame newGame = new ChessGame();
      GameData newGameData = new GameData(memoryGameDAO.getGames().size()+1,
              "","",
              createGameRequest.gameName(),newGame);

      memoryGameDAO.addGameData(newGameData);
      return new CreateGameResult(newGameData.gameID());
    }
    res.status(401);
    return new ErrorResult("Error: unauthorized");
  }
  public void clear(MemoryAuthDAO memoryAuthDAO, MemoryUserDAO memoryUserDAO, MemoryGameDAO memoryGameDAO){
    memoryAuthDAO.deleteAllAuthData();
    memoryUserDAO.deleteAllUsers();
    memoryGameDAO.deleteAllGames();
  }
  //Auth

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }

}
