package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class QueenMoves extends PieceMoves {

  public QueenMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
    super();
    this.board = board;
    this.piece=piece;
    this.myPosition =myPosition;
  }

  @Override
  public ArrayList<ChessMove> getPossibleMoves() {
    setDirection(2);
    return super.getPossibleMoves();
  }

  //1 if only one step,  if multiple
  @Override
  void setDirection(int cont)  {
    super.setDirection(cont);
  }
}
