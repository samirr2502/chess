package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoves {
  private final Collection<ChessMove> possibleChessMoves= new HashSet<>();
  private final ChessPosition myPosition;
  private final ChessBoard board;
  private final ChessGame.TeamColor color;
  public PawnMoves(ChessBoard board, ChessPosition myPosition,
                   ChessGame.TeamColor color){
      this.board = board;
      this.myPosition =myPosition;
      this.color =color;
  }
  private void addMove(ChessMove move){
    possibleChessMoves.add(move);

  }
  public Collection<ChessMove> getMoves(){
    setMoves();
    return possibleChessMoves;
  }

 /*
 * Set Moves function:
 * sets all the moves that the Pawn can make based on the different positions
 * it can be on the board, and if there are pieces to be eaten
 * Also checks if it can be promoted
 *
  */

  private void setMoves(){

    int goForward = myPosition.getRow()+1;
    int goForward2 =0;
    int goLeft = myPosition.getColumn()-1;
    int goRight = myPosition.getColumn()+1;
    ChessPosition forwardMove;
    ChessPosition forwardMove2 = null;
    ChessPosition rightMove;
    ChessPosition leftMove;

    if (color== ChessGame.TeamColor.BLACK) {
        goForward=myPosition.getRow()-1;
        if(myPosition.getRow() ==7) {
          goForward2 = myPosition.getRow() - 2;
        }
    } else {
      if(myPosition.getRow() ==2) {
        goForward2 = myPosition.getRow() + 2;
      }
    }

      forwardMove= new ChessPosition(goForward, myPosition.getColumn());
      if (goForward2!=0){ forwardMove2 = new ChessPosition(goForward2,myPosition.getColumn());}
      rightMove = new ChessPosition(goForward, goRight);
      leftMove = new ChessPosition(goForward, goLeft);
      pawnMoves(board, myPosition, leftMove, rightMove,forwardMove,forwardMove2);


  }
  /*Helper Function for set moves
   *Gets key information to move either the white or black piece to the corners in order ot eat
   * */
  private void pawnMoves(ChessBoard board, ChessPosition myPosition,
                         ChessPosition left, ChessPosition right,
                         ChessPosition forward, ChessPosition forward2){

    //Moves forward if there's no piece in front of it
    if (board.getPiece(forward) == null) {
      addPromotions(myPosition,forward,forward);
    }
    //Checks if piece can move 2 spots
    if (forward2!=null && board.getPiece(forward2) == null && board.getPiece(forward)==null) {
      addMove(new ChessMove(myPosition, forward2, null));
    }

    //Checks if not at the edge and if there's a piece to eat
    if (myPosition.getColumn() != 1 && (board.getPiece(right) != null && board.getPiece(right).pieceColor != color)){
      addPromotions(myPosition,right,forward);
    }
    //Checks if not at the edge and if there's a piece to eat
    if (myPosition.getColumn() != 8 && (board.getPiece(left) != null && board.getPiece(left).pieceColor != color)){
      addPromotions(myPosition,left,forward);

    }
  }
  private void addPromotions(ChessPosition myPosition,
                             ChessPosition direction, ChessPosition forward){
    if(forward.getRow()==8 && color== ChessGame.TeamColor.WHITE
            || forward.getRow() ==1 && color == ChessGame.TeamColor.BLACK) {
      addMove(new ChessMove(myPosition, direction, ChessPiece.PieceType.QUEEN));
      addMove(new ChessMove(myPosition, direction, ChessPiece.PieceType.BISHOP));
      addMove(new ChessMove(myPosition, direction, ChessPiece.PieceType.ROOK));
      addMove(new ChessMove(myPosition, direction, ChessPiece.PieceType.KNIGHT));
    } else {
      addMove(new ChessMove(myPosition, direction, null));

    }
  }
}
