package service;

import chess.ChessGame;

public class JoinGameRequest {
  String playerColor;
  int gameID;
  public JoinGameRequest(String playerColor, int gameID){
    this.playerColor=playerColor;
    this.gameID=gameID;
  }
}
