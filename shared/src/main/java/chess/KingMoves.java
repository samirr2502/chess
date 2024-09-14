package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMoves {
  private final Collection<ChessMove> possibleChessMoves= new HashSet<>();
  private final ChessPosition myPosition;
  private final ChessBoard board;
  private final ChessGame.TeamColor color;
  public KingMoves(ChessBoard board, ChessPosition myPosition,
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
    int goBackward =myPosition.getRow()-1;
    int goLeft = myPosition.getColumn()-1;
    int goRight = myPosition.getColumn()+1;


    ChessPosition forwardMove= new ChessPosition(goForward, myPosition.getColumn());
    ChessPosition backwardMove =new ChessPosition(goBackward,myPosition.getColumn());
    ChessPosition rightMove = new ChessPosition(myPosition.getRow(), goRight);
    ChessPosition leftMove = new ChessPosition(myPosition.getRow(), goLeft);
    ChessPosition rightForwardMove = new ChessPosition(goForward, goRight);
    ChessPosition leftForwardMove = new ChessPosition(goForward, goLeft);
    ChessPosition rightBackwardMove = new ChessPosition(goBackward, goRight);
    ChessPosition leftBackwardMove = new ChessPosition(goBackward, goLeft);

    kingMoves(board, myPosition,
            forwardMove,backwardMove,
            rightMove, leftMove,
            rightForwardMove, leftForwardMove,
            rightBackwardMove, leftBackwardMove
            );
  }
  /*Helper Function for set moves
   *Gets key information to move either the white or black piece to the corners in order ot eat
   * */
  private void kingMoves(ChessBoard board, ChessPosition myPosition,
                         ChessPosition forward, ChessPosition backward,
                         ChessPosition right, ChessPosition left,
                         ChessPosition rightForward, ChessPosition leftForward,
                         ChessPosition rightBackward, ChessPosition leftBackward
                         ){

    //Moves forward if it's not at the edge and there are no team piece is in front of it
    if (myPosition.getRow()!=8 ) {
      if(board.getPiece(forward) ==null) {
        addMove(new ChessMove(myPosition, forward, null));
      } else if (board.getPiece(forward).pieceColor != color){
        addMove(new ChessMove(myPosition, forward, null));
      }
    }
    //moves backwards if it's not at the edge and there are no team pieces in behind of it
    if (myPosition.getRow()!=1) {
      if (board.getPiece(backward) ==null) {
        addMove(new ChessMove(myPosition, backward, null));
      } else if ( board.getPiece(backward).pieceColor != color){
        addMove(new ChessMove(myPosition, backward, null));
      }
    }

    //Checks if not at the edge and if there's a piece to eat
    if (myPosition.getColumn() != 8){
      if (board.getPiece(right) ==null ) {
        addMove(new ChessMove(myPosition, right, null));
      } else if (board.getPiece(right).pieceColor != color){
        addMove(new ChessMove(myPosition, right, null));
      }
    }
    //Checks if not at the edge and if there's a piece to eat
    if (myPosition.getColumn() != 1){
      if (board.getPiece(left) ==null ) {
        addMove(new ChessMove(myPosition, left, null));
      } else if (board.getPiece(left).pieceColor != color){
        addMove(new ChessMove(myPosition, left, null));
      }
    }
    //Moves forward if it's not at the edge and there are no team piece is in front of it
    if (myPosition.getRow()!=8  && myPosition.getColumn()!=8) {
     if (board.getPiece(rightForward) ==null) {
       addMove(new ChessMove(myPosition, rightForward, null));
     } else if (board.getPiece(rightForward).pieceColor != color){
       addMove(new ChessMove(myPosition, rightForward, null));
     }
    }
    //moves backwards if it's not at the edge and there are no team pieces in behind of it
    if (myPosition.getRow()!=8  && myPosition.getColumn()!=1) {
      if (board.getPiece(leftForward) ==null) {
        addMove(new ChessMove(myPosition, leftForward, null));
      } else if (board.getPiece(leftForward).pieceColor != color){
        addMove(new ChessMove(myPosition, leftForward, null));
      }
    }

    //Checks if not at the edge and if there's a piece to eat
    if (myPosition.getRow()!=1  && myPosition.getColumn()!=8) {
      if (board.getPiece(rightBackward) ==null) {
        addMove(new ChessMove(myPosition, rightBackward, null));
      } else if (board.getPiece(rightBackward).pieceColor != color){
        addMove(new ChessMove(myPosition, rightBackward, null));
      }
    }
    //Checks if not at the edge and if there's a piece to eat
    if (myPosition.getRow()!=1  && myPosition.getColumn()!=1) {
      if (board.getPiece(leftBackward) ==null) {
        addMove(new ChessMove(myPosition, leftBackward, null));
      } else if (board.getPiece(leftBackward).pieceColor != color){
        addMove(new ChessMove(myPosition, leftBackward, null));
      }
    }
  }
}
