package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopMoves extends PieceMovesAbs {

  public BishopMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
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
    setMoves(cont, 1, 1,promoPiece);//Move topRight
    setMoves(cont, 1, -1,promoPiece);//Move topLeft
    setMoves(cont, -1, -1,promoPiece);//Move bottomLeft
    setMoves(cont, -1, 1,promoPiece);//Move bottomRight
  }
}
