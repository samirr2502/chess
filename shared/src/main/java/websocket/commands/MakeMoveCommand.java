package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand {
  public ChessMove move;
  public MakeMoveCommand(ChessMove move){

    this.move = move;
  }
}
