package chess.piecesMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class CrossMoves {
  public ArrayList<ChessMove> possibleChessMoves;

  private final ChessPosition myPosition;
  private final ChessBoard board;
  private final ChessGame.TeamColor color;
  public CrossMoves(ArrayList<ChessMove> possibleChessMoves,
                    ChessPosition myPosition, ChessBoard board,
                    ChessGame.TeamColor color){
    this.possibleChessMoves =possibleChessMoves;
    this.myPosition = myPosition;
    this.board = board;
    this.color = color;
  }
  private void addMove(ChessMove move){
    possibleChessMoves.add(move);

  }
  public void setMoves(){

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
      } else if (board.getPiece(direction) != null && board.getPiece(direction).getTeamColor() != color) {
        addMove(new ChessMove(myPosition, direction, null));
        break;
      } else{
        break;
      }
    }

  }
}
