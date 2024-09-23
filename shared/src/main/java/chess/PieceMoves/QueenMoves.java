package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.PieceMoves.PieceMovesAbs;

import java.util.ArrayList;

public class QueenMoves extends PieceMovesAbs {

  public QueenMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
    super();
    this.board = board;
    this.piece=piece;
    this.myPosition =myPosition;
  }

  @Override
  public ArrayList<ChessMove> getPossibleMoves() {
    setDirection(2,null);
    return super.getPossibleMoves();
  }

  //1 if only one step,  if multiple
  @Override
  void setDirection(int cont, ChessPiece.PieceType promoPiece)  {
    super.setDirection(cont, null);
  }
}
