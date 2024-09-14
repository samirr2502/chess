package chess.piecesMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMoves {
  private final ArrayList<ChessMove> possibleChessMoves = new ArrayList<>();
  private final ChessPosition myPosition;
  private final ChessBoard board;
  private final ChessGame.TeamColor color;

  public RookMoves(ChessBoard board, ChessPosition myPosition,
                   ChessGame.TeamColor color) {
    this.board = board;
    this.myPosition = myPosition;
    this.color = color;
  }

  public ArrayList<ChessMove> getMoves() {
    CrossMoves crossMoves = new CrossMoves(possibleChessMoves, myPosition, board, color);
    crossMoves.setMoves();
    return crossMoves.possibleChessMoves;
  }
}