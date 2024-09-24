package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class KnightMoves extends PieceMoves {

  public KnightMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
    super();
    this.board = board;
    this.piece=piece;
    this.myPosition =myPosition;
  }

  @Override
  public ArrayList<ChessMove> getPossibleMoves() {
    setDirection(1);
    return super.getPossibleMoves();
  }

  //1 if only one step,  if multiple
  @Override
  void setDirection(int rule)  {
    setMoves(rule, 2, 1);//Move RightTop (1right 2 Top)
    setMoves(rule, 1, 2);//Move TopRight
    setMoves(rule, -1, 2);//Move 1Bottom2Right
    setMoves(rule, -2, 1);//Move 1Right2Bottom
    setMoves(rule, -2, -1);//Move 1Left2Bottom
    setMoves(rule, -1, -2);//Move 1Bottom2Left
    setMoves(rule, 1, -2);//Move 1Top2Left
    setMoves(rule, 2, -1);//Move 1Left2Top
  }
}
