package chess.piecesMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

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

    //Set new Positions
    ChessPosition forwardMove= new ChessPosition(goForward, myPosition.getColumn());
    ChessPosition backwardMove =new ChessPosition(goBackward,myPosition.getColumn());
    ChessPosition rightMove = new ChessPosition(myPosition.getRow(), goRight);
    ChessPosition leftMove = new ChessPosition(myPosition.getRow(), goLeft);
    ChessPosition rightForwardMove = new ChessPosition(goForward, goRight);
    ChessPosition leftForwardMove = new ChessPosition(goForward, goLeft);
    ChessPosition rightBackwardMove = new ChessPosition(goBackward, goRight);
    ChessPosition leftBackwardMove = new ChessPosition(goBackward, goLeft);

    /*Create Moves if meets criteria
    * Not at the edge.
    * Not landing on team's color piece
     */
    kingMoves(forwardMove, 8, 0);
    kingMoves(backwardMove,1,0);
    kingMoves(rightMove,0,8);
    kingMoves(leftMove, 0, 1);
    kingMoves(rightForwardMove,8,8);
    kingMoves(leftForwardMove,8,1);
    kingMoves(rightBackwardMove,1,8);
    kingMoves(leftBackwardMove,1,1);

  }

    /*Helper Function for set moves
    *Checks if new move will meet criteria before adding it to the ArrayList
    */
    private void kingMoves (ChessPosition direction,int rowEdge, int colEdge){

        if ((myPosition.getRow() != rowEdge && myPosition.getColumn() !=colEdge) && //Checks edges
                (board.getPiece(direction) ==null ||
                        board.getPiece(direction).getTeamColor() != color)) {
            addMove(new ChessMove(myPosition, direction, null));
          }
  }
}
