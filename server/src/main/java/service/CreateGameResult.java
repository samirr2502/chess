package service;

import server.CreateGameRequest;

public class CreateGameResult extends Result{
  int gameID;
  public CreateGameResult(int gameID){
    this.gameID = gameID;
  }
}
