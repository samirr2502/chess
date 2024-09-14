package chess.piecesMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMoves {
  private final Collection<ChessMove> possibleChessMoves= new HashSet<>();
  private final ChessPosition myPosition;
  private final ChessBoard board;
  private final ChessGame.TeamColor color;
  public KnightMoves(ChessBoard board, ChessPosition myPosition,
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

    //Set up Chess positions
    ChessPosition forwardTopRightMove= new ChessPosition(goForward+1, goRight);
    ChessPosition forwardBottomRightMove =new ChessPosition(goForward,goRight+1);
    ChessPosition forwardTopLeftMove = new ChessPosition(goForward+1, goLeft);
    ChessPosition forwardBottomLeftMove = new ChessPosition(goForward, goLeft-1);
    ChessPosition backwardTopRightMove = new ChessPosition(goBackward-1, goRight);
    ChessPosition backwardBottomRightMove = new ChessPosition(goBackward, goRight+1);
    ChessPosition backwardTopLeftMove = new ChessPosition(goBackward-1, goLeft);
    ChessPosition backwardBottomLeftMove = new ChessPosition(goBackward, goLeft-1);


    //Check if falls outside edge
    boolean forwardTopRightInside = forwardTopRightMove.getRow() <=8 && forwardTopRightMove.getColumn() <=8;
    boolean forwardBottomRightInside =forwardBottomRightMove.getRow() <=8 && forwardBottomRightMove.getColumn() <=8;
    boolean forwardTopLeftInside = forwardTopLeftMove.getRow() <=8 && forwardTopLeftMove.getColumn() >=1;
    boolean forwardBottomLeftInside = forwardBottomLeftMove.getRow() <=8 && forwardBottomLeftMove.getColumn() >=1;
    boolean backwardTopRightInside = backwardTopRightMove.getRow() >=1 && backwardTopRightMove.getColumn() <=8;
    boolean backwardBottomRightInside = backwardBottomRightMove.getRow() >=1&& backwardBottomRightMove.getColumn() <=8;
    boolean backwardTopLeftInside = backwardTopLeftMove.getRow() >=1 && backwardTopLeftMove.getColumn() >=1;
    boolean backwardBottomLeftInside = backwardBottomLeftMove.getRow() >=1 && backwardBottomLeftMove.getColumn() >=1;

    //Add the moves if they meet the criteria
    knightMoves(forwardTopRightMove, forwardTopRightInside);
    knightMoves(forwardBottomRightMove,forwardBottomRightInside);
    knightMoves(forwardTopLeftMove,forwardTopLeftInside);
    knightMoves(forwardBottomLeftMove,forwardBottomLeftInside);
    knightMoves(backwardTopRightMove,backwardTopRightInside);
    knightMoves(backwardBottomRightMove, backwardBottomRightInside);
    knightMoves(backwardTopLeftMove,backwardTopLeftInside);
    knightMoves(backwardBottomLeftMove,backwardBottomLeftInside);
  }
  /*Helper Function for set moves
   *Gets key information to move either the white or black piece to the corners in order ot eat
   * */
  private void knightMoves(ChessPosition newPosition, boolean insideBoard){
    if (insideBoard &&
            (board.getPiece(newPosition) == null ||
            (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() !=color))){
      addMove(new ChessMove(myPosition,newPosition,null));
    }
  }
}
