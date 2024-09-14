package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class RookMoves {
  private final Collection<ChessMove> possibleChessMoves= new HashSet<>();
  private final ChessPosition myPosition;
  private final ChessBoard board;
  private final ChessGame.TeamColor color;
  public RookMoves(ChessBoard board, ChessPosition myPosition,
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

    int incrementer=1;


    ArrayList<ChessPosition> forwardPositions = new ArrayList<>();
    ArrayList<ChessPosition> backwardPositions = new ArrayList<>();
    ArrayList<ChessPosition> rightPositions = new ArrayList<>();
    ArrayList<ChessPosition> leftPositions = new ArrayList<>();

    for (int i = myPosition.getRow()+1; i <= 8; i++) {
      forwardPositions.add(new ChessPosition(myPosition.getRow()+incrementer, myPosition.getColumn()));
      incrementer++;
    }
    incrementer =1;
    for (int i = 1; i < myPosition.getRow(); i++) {
      backwardPositions.add(new ChessPosition(myPosition.getRow()-incrementer, myPosition.getColumn()));
      incrementer++;
    }
    incrementer =1;
    for (int i = myPosition.getColumn()+1; i <= 8; i++) {
      rightPositions.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+incrementer));
      incrementer++;
    }
    incrementer =1;
    for (int i = 1; i < myPosition.getColumn(); i++) {
      leftPositions.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-incrementer));
      incrementer++;
    }

    rookMoves(forwardPositions,backwardPositions,
            rightPositions,leftPositions
            );
  }
  /*Helper Function for set moves
   *Gets key information to move either the white or black piece to the corners in order ot eat
   * */
  private void rookMoves(ArrayList<ChessPosition> forwardPositions,ArrayList<ChessPosition> backwardPositions,
                         ArrayList<ChessPosition> rightPositions, ArrayList<ChessPosition> leftPositions  ) {

    //Moves forward if it's not at the edge and there are no team piece is in front of it
    moveRookDirection(forwardPositions);
    moveRookDirection(backwardPositions);
    moveRookDirection(rightPositions);
    moveRookDirection(leftPositions);
    //moves backwards if it's not at the edge and there are no team pieces in behind of it
  }
  public void moveRookDirection(Collection<ChessPosition> directionPositions){
    for(ChessPosition direction : directionPositions) {
      if (board.getPiece(direction)== null) {
        addMove(new ChessMove(myPosition, direction, null));
      } else if (board.getPiece(direction) != null && board.getPiece(direction).pieceColor != color) {
        addMove(new ChessMove(myPosition, direction, null));
        break;
      } else{
        break;
      }
    }
  }
}
