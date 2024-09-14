package chess.piecesMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class QueenMoves {
  private final ArrayList<ChessMove> possibleChessMoves = new ArrayList<>();
  private final ChessPosition myPosition;
  private final ChessBoard board;
  private final ChessGame.TeamColor color;

  public QueenMoves(ChessBoard board, ChessPosition myPosition,
                    ChessGame.TeamColor color) {
    this.board = board;
    this.myPosition = myPosition;
    this.color = color;
  }

  public ArrayList<ChessMove> getMoves() {
    DiagonalMoves diagonalMoves = new DiagonalMoves(possibleChessMoves,myPosition,board,color);
    diagonalMoves.setMoves();
    CrossMoves crossMoves = new CrossMoves(possibleChessMoves,myPosition,board,color);
    crossMoves.setMoves();
    return possibleChessMoves;
  }

}
