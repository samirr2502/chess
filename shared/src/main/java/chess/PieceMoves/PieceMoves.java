package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;

/**
 * PieceMoves abstract class
 * Contains:
 * Rules Methods:
 *    ->Rule_1() for pieces that move only one step (Kings, Knights)
 *    ->Rule_2() for pieces that move continually (Queen, Bishop, Tower)
 *    ->Rule_3() for pieces that have a special rule (Pawn)

 * Validation Methods
 *    ->ValidateMoves() Checks if new piece is inside the edges
 *    ->validatePromotion, validateRule3_0,validateRule3_1, validate Rule3_2:
 *                Validations for Rule_3()

 * Helper Methods
 *    ->PromotionPiece() Creates the promotionPiece moves;
 *    ->IncrementRowOrCol() Adds or subtracts one from row/col for Rule_2()

 * SetMoves() Handles the rule cases
 * SetDirection() It's overridden by inheritance classes for implementation
 */
public abstract class PieceMoves {
  ArrayList<ChessMove> possibleMoves = new ArrayList<>();
  ChessBoard board;
  ChessPiece piece;
  ChessPosition myPosition;

  public ArrayList<ChessMove> getPossibleMoves(){
    return possibleMoves;
  }

  /**
   * Rule for pieces moving one step (King, Knight)
   * @param moveRow Vertical Direction
   * @param moveCol Horizontal Direction
   */
  void moveRule_1(int moveRow, int moveCol){
    ChessPosition newPosition = new ChessPosition(myPosition.getRow() + moveRow, myPosition.getColumn() + moveCol);
    if (validateMoves(newPosition) &&validateNoTeamPiece(newPosition)) {
      possibleMoves.add(new ChessMove(myPosition, newPosition, null));
    }
  }

  /**
   * Rule for pieces moving continuously step (Queen, Rook, Bishop)
   * @param moveRow Vertical Direction
   * @param moveCol Horizontal Direction
   */
  void moveRule_2(int moveRow, int moveCol){
    //int edge = rowEdge == 0 ? colEdge: rowEdge;
    for (int i=0; i<8; i++) {
      ChessPosition newPosition = new ChessPosition(myPosition.getRow() + moveRow, myPosition.getColumn() + moveCol);
      if (validateMoves(newPosition) &&validateNoTeamPiece(newPosition)) {
        possibleMoves.add(new ChessMove(myPosition, newPosition, null));
        if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != piece.getTeamColor()) {
          break;
        }
      } else {
        break;
      }
      moveCol += incrementRowCol(moveCol);
      moveRow += incrementRowCol(moveRow);
    }
  }
  /**
   * Rule for pieces with Special moves (Pawn)
   * @param moveRow Vertical Direction
   * @param moveCol Horizontal Direction
   */
  void moveRule_3(int moveRow, int moveCol){
    ChessPosition newPosition = new ChessPosition(myPosition.getRow() + moveRow, myPosition.getColumn() + moveCol);
    if (newPosition.getColumn() == myPosition.getColumn()) {//Checks if piece is moving forward
      //Check if piece has valid moves
      if (validateMovesRule_3_0(newPosition)) {
        if(validatePromotionRule(newPosition)){
          promotePiece(newPosition);
        }else {
          possibleMoves.add(new ChessMove(myPosition, newPosition, null));
        }
      }
      //Check if piece can move twice in first move
      ChessPosition newPosition2 = new ChessPosition(myPosition.getRow() +(moveRow*2), myPosition.getColumn() + moveCol);
      if (validateMovesRule_3_1() && validateMovesRule_3_0(newPosition2)&& validateNoTeamPiece(newPosition)&& validateMoves(newPosition)) {
        possibleMoves.add(new ChessMove(myPosition, newPosition2, null));
      }
    } else { //if piece is moving to the top_right or top_left
      if (validateMovesRule_3_2(newPosition)){
        if(validatePromotionRule(newPosition)){
          promotePiece(newPosition);
        }else {
          possibleMoves.add(new ChessMove(myPosition, newPosition, null));
        }
      }
    }
  }


  boolean validateMoves(ChessPosition newPosition){
    var insideRows = newPosition.getRow()<=8 && newPosition.getRow()>=1;
    var insideColumns = newPosition.getColumn()<=8 && newPosition.getColumn()>=1;
    return insideRows && insideColumns;
  }
  boolean validateNoTeamPiece(ChessPosition newPosition){
      return board.getPiece(newPosition) == null ||
              board.getPiece(newPosition).getTeamColor() != piece.getTeamColor();

  }
  boolean validatePromotionRule(ChessPosition newPosition){
    var whiteEnd = newPosition.getRow()==8 && piece.getTeamColor() == ChessGame.TeamColor.WHITE;
    var blackEnd = newPosition.getRow()==1 && piece.getTeamColor() == ChessGame.TeamColor.BLACK;
    return whiteEnd || blackEnd;
  }
  boolean validateMovesRule_3_0(ChessPosition newPosition){
    return board.getPiece(newPosition) == null;

  }
  boolean validateMovesRule_3_1(){
    var whiteStartPosition = myPosition.getRow() ==2 && piece.getTeamColor() == ChessGame.TeamColor.WHITE;
    var blackStartPosition = myPosition.getRow() ==7 && piece.getTeamColor() == ChessGame.TeamColor.BLACK;
    return whiteStartPosition || blackStartPosition;
  }
  boolean validateMovesRule_3_2(ChessPosition newPosition){
    if (newPosition.getColumn()!=myPosition.getColumn()) {
      return board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != piece.getTeamColor();
    }
    return board.getPiece(newPosition) == null;
  }

  /*
  Helper Functions: PromotePiece and IncrementRowCol
   */

  void promotePiece(ChessPosition newPosition){
    possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
    possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
    possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
    possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
  }
  int incrementRowCol(int rowOrCol){
    if (rowOrCol !=0) {
      if(rowOrCol >0)
        return 1;
      else
        return -1;
    }
    return 0;
  }

  void setMoves(int rule, int moveRow, int moveCol){
    switch (rule){
      case 1:
        moveRule_1(moveRow,moveCol);
        break;
      case 2:
        moveRule_2(moveRow,moveCol);
        break;
      case 3:
        moveRule_3(moveRow,moveCol);
        break;
    }
  }

  void setDirection(int rule) {

    //Move all directions
    setMoves(rule, 0, 1);//Move right
    setMoves(rule, 1, 1);//Move topRight
    setMoves(rule, 1, 0);//Move top
    setMoves(rule, 1, -1);//Move topLeft
    setMoves(rule, 0, -1);//Move left
    setMoves(rule, -1, -1);//Move bottomLeft
    setMoves(rule, -1, 0);//Move bottom
    setMoves(rule, -1, 1);//Move bottomRight
  }

}
