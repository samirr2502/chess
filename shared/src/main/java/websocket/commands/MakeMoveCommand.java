package websocket.commands;

import chess.ChessMove;
import com.google.gson.Gson;

public class MakeMoveCommand {
  public ChessMove move;
  public MakeMoveCommand(String jsonMove){

    this.move = new Gson().fromJson(jsonMove, ChessMove.class);
  }
}
