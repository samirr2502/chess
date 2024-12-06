package chess.piecemoves;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import static chess.piecemoves.ValidateMoves.*;
public abstract class PieceMovesRules {

  /**
   * Add a move if move is available
   * Works for one-step pieces (King, Knight)
   * @param pieceMoves current PieceMovesObject
   * @param moveRow Vertical direction
   * @param moveCol horizontal direction
   */
  public static void movesRule1(PieceMoves pieceMoves, int moveRow, int moveCol){
    ChessPosition newPosition = new ChessPosition(pieceMoves.myPosition.getRow() +moveRow,
            pieceMoves.myPosition.getColumn()+moveCol);


    if (validateEdges(newPosition)) {
      boolean empty = validateEmptyPosition(pieceMoves, newPosition);
      if (empty) {
        pieceMoves.possibleMoves.add(new ChessMove(pieceMoves.myPosition, newPosition, null));
      } else {
        boolean take = validateTakeTeamPiece(pieceMoves, newPosition);
        if (take) {
          pieceMoves.possibleMoves.add(new ChessMove(pieceMoves.myPosition, newPosition, null));

        } else {

        }
      }

    }
  }

  /**
   * Used for pieces that have continuous movement (Queen, Bishop, Rooks)
   * @param pieceMoves current Piece Moves object
   */
  public static void movesRule2(PieceMoves pieceMoves, int moveRow, int moveCol){
    for (int i=1; i <=8; i++){
      ChessPosition newPosition = new ChessPosition(pieceMoves.myPosition.getRow()+moveRow,
              pieceMoves.myPosition.getColumn()+moveCol);
      if(validateEdges(newPosition)
              && (validateEmptyPosition(pieceMoves,newPosition) || validateTakeTeamPiece(pieceMoves, newPosition))){
        pieceMoves.possibleMoves.add(new ChessMove(pieceMoves.myPosition, newPosition,null));
        if (!validateEmptyPosition(pieceMoves, newPosition)){
          break;
        }
      }else {
        break;
      }
      moveRow += incrementRowCol(moveRow);
      moveCol += incrementRowCol(moveCol);
    }

  }
  public  static void movesRule3(PieceMoves pieceMoves, int moveRow, int moveCol){
    ChessPosition newPosition = new ChessPosition(pieceMoves.myPosition.getRow()+moveRow,
            pieceMoves.myPosition.getColumn()+moveCol);

    //1.) Check if moving forward
    if (pieceMoves.myPosition.getColumn() == newPosition.getColumn()){
      //Check if nothing in front of it
      if(validateEdges(newPosition) && validateEmptyPosition(pieceMoves,newPosition)){
        //Check if piece can be promoted
        if(validatePromotionRule(pieceMoves,newPosition)){
          promotePiece(pieceMoves,newPosition);
        }else {
          pieceMoves.possibleMoves.add(new ChessMove(pieceMoves.myPosition, newPosition, null));
        }
      }
      //Check if piece can move twice
      if(validateInitialMove(pieceMoves)){
        ChessPosition newPosition2 = new ChessPosition(pieceMoves.myPosition.getRow()+(2*moveRow),
                pieceMoves.myPosition.getColumn()+moveCol);
        if(validateEmptyPosition(pieceMoves,newPosition) && validateEmptyPosition(pieceMoves,newPosition2)){
            pieceMoves.possibleMoves.add(new ChessMove(pieceMoves.myPosition, newPosition2, null));
          }

      }
    //2.) Check if moving to the sides
    }else {
      if(validateEdges(newPosition) && validatePawnTakePiece(pieceMoves,newPosition)) {
        if (validatePromotionRule(pieceMoves, newPosition)) {
          promotePiece(pieceMoves, newPosition);
        } else {
          pieceMoves.possibleMoves.add(new ChessMove(pieceMoves.myPosition, newPosition, null));
        }
      }
    }
  }

  //Helper Functions
  public static int incrementRowCol(int rowCol){
    if(rowCol!=0) {
      rowCol = rowCol >0? 1: -1;
    }
    return rowCol;
  }
  public static void promotePiece(PieceMoves pieceMoves, ChessPosition newPosition){
    pieceMoves.possibleMoves.add(new ChessMove(pieceMoves.myPosition,newPosition, ChessPiece.PieceType.QUEEN));
    pieceMoves.possibleMoves.add(new ChessMove(pieceMoves.myPosition,newPosition, ChessPiece.PieceType.KNIGHT));
    pieceMoves.possibleMoves.add(new ChessMove(pieceMoves.myPosition,newPosition, ChessPiece.PieceType.BISHOP));
    pieceMoves.possibleMoves.add(new ChessMove(pieceMoves.myPosition,newPosition, ChessPiece.PieceType.ROOK));
  }
}
