package service;

import chess.ChessGame;

public class JoinGameRequest {
  String playerColor;
  int gameID;
  public JoinGameRequest(String color, int gameID){
    this.playerColor=color;
    this.gameID=gameID;
  }
}
