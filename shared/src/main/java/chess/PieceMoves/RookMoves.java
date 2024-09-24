package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMoves extends PieceMoves {

  public RookMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
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
  void setDirection(int rule)  {
    setMoves(rule, 0, 1);//Move right
    setMoves(rule, 1, 0);//Move top
    setMoves(rule, 0, -1);//Move left
    setMoves(rule, -1, 0);//Move bottom
  }
}
