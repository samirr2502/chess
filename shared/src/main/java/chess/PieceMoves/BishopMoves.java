package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopMoves extends PieceMoves {

  public BishopMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
    super();
    this.board = board;
    this.piece=piece;
    this.myPosition =myPosition;
  }

  @Override
  public ArrayList<ChessMove> getPossibleMoves() {
    setDirection(2); //See rule 2 in Parent class
    return super.getPossibleMoves();
  }

  @Override
  void setDirection(int rule)  {
    setMoves(rule, 1, 1);//Move topRight
    setMoves(rule, 1, -1);//Move topLeft
    setMoves(rule, -1, -1);//Move bottomLeft
    setMoves(rule, -1, 1);//Move bottomRight
  }
}
