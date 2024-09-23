package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class KnightMoves extends PieceMovesAbs {

  public KnightMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
    super();
    this.board = board;
    this.piece=piece;
    this.myPosition =myPosition;
  }

  @Override
  public ArrayList<ChessMove> getPossibleMoves() {
    setDirection(1,null);
    return super.getPossibleMoves();
  }

  //1 if only one step,  if multiple
  @Override
  void setDirection(int cont, ChessPiece.PieceType promoPiece)  {
    setMoves(cont, 2, 1,promoPiece);//Move RightTop (1right 2 Top)
    setMoves(cont, 1, 2,promoPiece);//Move TopRight
    setMoves(cont, -1, 2,promoPiece);//Move 1Bottom2Right
    setMoves(cont, -2, 1,promoPiece);//Move 1Right2Bottom
    setMoves(cont, -2, -1,promoPiece);//Move 1Left2Bottom
    setMoves(cont, -1, -2,promoPiece);//Move 1Bottom2Left
    setMoves(cont, 1, -2,promoPiece);//Move 1Top2Left
    setMoves(cont, 2, -1,promoPiece);//Move 1Left2Top
  }
}
