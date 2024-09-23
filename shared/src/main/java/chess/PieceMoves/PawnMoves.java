package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;

public class PawnMoves extends PieceMovesAbs {

  public PawnMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
    super();
    this.board = board;
    this.piece=piece;
    this.myPosition =myPosition;
  }

  @Override
  public ArrayList<ChessMove> getPossibleMoves() {
    setDirection(3,null);
    return super.getPossibleMoves();
  }

  //1 if only one step,  if multiple
  @Override
  void setDirection(int cont, ChessPiece.PieceType promoPiece)  {
    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
      setMoves(cont,1,0,null); //move forward
      setMoves(cont, 1,1,null);//move forward right
      setMoves(cont, 1,-1,null); //move forward left
    } else  {
      setMoves(cont,-1,0,null); //move down
      setMoves(cont, -1,1,null);//move down right
      setMoves(cont, -1,-1,null); //move down left
    }

  }
}
