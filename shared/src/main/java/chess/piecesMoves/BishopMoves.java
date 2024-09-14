package chess.piecesMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopMoves {
  private final ArrayList<ChessMove> possibleChessMoves= new ArrayList<>();
  private final ChessPosition myPosition;
  private final ChessBoard board;
  private final ChessGame.TeamColor color;
  public BishopMoves(ChessBoard board, ChessPosition myPosition,
                     ChessGame.TeamColor color){
    this.board = board;
    this.myPosition =myPosition;
    this.color =color;
  }
  public ArrayList<ChessMove> getMoves(){
    //Create Diagonal Moves
    DiagonalMoves diagonalMoves = new DiagonalMoves(possibleChessMoves,myPosition,board,color);
    diagonalMoves.setMoves();
    return diagonalMoves.possibleChessMoves;


  /*
   * Set Moves function:
   * sets all the moves that the Pawn can make based on the different positions
   * it can be on the board, and if there are pieces to be eaten
   * Also checks if it can be promoted
   */

  }
}
