package chess.pieceMoves;

import chess.*;

import java.util.ArrayList;

public class PawnMoves extends PieceMoves {

  public PawnMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
    super();
    this.board = board;
    this.piece=piece;
    this.myPosition =myPosition;
  }

  @Override
  public ArrayList<ChessMove> getPossibleMoves() {
    setDirection(3);
    return super.getPossibleMoves();
  }

  //1 if only one step,  if multiple
  @Override
  void setDirection(int rule)  {
    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
      setMoves(rule,1,0); //move forward
      setMoves(rule, 1,1);//move forward right
      setMoves(rule, 1,-1); //move forward left
    } else  {
      setMoves(rule,-1,0); //move down
      setMoves(rule, -1,1);//move down right
      setMoves(rule, -1,-1); //move down left
    }

  }
}
