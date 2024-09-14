package chess.piecesMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class BishopMoves {
  private final Collection<ChessMove> possibleChessMoves= new HashSet<>();
  private final ChessPosition myPosition;
  private final ChessBoard board;
  private final ChessGame.TeamColor color;
  public BishopMoves(ChessBoard board, ChessPosition myPosition,
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


    ArrayList<ChessPosition> forwardRightPositions = new ArrayList<>();
    ArrayList<ChessPosition> forwardLeftPositions = new ArrayList<>();
    ArrayList<ChessPosition> backwardRightPositions = new ArrayList<>();
    ArrayList<ChessPosition> backwardLeftPositions = new ArrayList<>();

    //Diagonal moves
    for (int i = 1; i <= 8; i++) {
      if(myPosition.getRow()+i >8 || myPosition.getColumn()+i>8){break;}
      forwardRightPositions.add(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i));

    }
    for (int i = 1; i <=8; i++) {
      if(myPosition.getRow()+i >8 || myPosition.getColumn()-i<1){break;}
      forwardLeftPositions.add(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i));
    }
    for (int i =1; i <= 8; i++) {
      if(myPosition.getRow()-i <1 || myPosition.getColumn()+i>8){break;}
      backwardRightPositions.add(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i));
    }
    for (int i = 1; i <=8; i++) {
      if(myPosition.getRow()-i <1 || myPosition.getColumn()-i<1){break;}
      backwardLeftPositions.add(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i));
    }

    moveQueenDirection(forwardRightPositions);
    moveQueenDirection(forwardLeftPositions);
    moveQueenDirection(backwardRightPositions);
    moveQueenDirection(backwardLeftPositions);
  }
  /*Helper Function for set moves
   *Gets key information to move either the white or black piece to the corners in order ot eat
   * */
  public void moveQueenDirection(ArrayList<ChessPosition> directionPositions){
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
