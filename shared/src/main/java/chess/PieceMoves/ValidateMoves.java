package chess.PieceMoves;

import chess.*;

public abstract class ValidateMoves {

  /**
   *
   * @param newPosition new Position
   * @return true if within edges
   */
  public static boolean validateEdges(ChessPosition newPosition){
    var insideRow = newPosition.getRow() <=8 && newPosition.getRow() >=1;
    var insideCol =newPosition.getColumn() <=8 && newPosition.getColumn() >=1;
    return insideRow && insideCol;
  }

  /**
   *
   * @param pieceMoves PieceMoves Object
   * @param newPosition New Position
   * @return true if empty or piece is not own team's
   */
  public static boolean validateEmptyPosition(PieceMoves pieceMoves, ChessPosition newPosition){
    return pieceMoves.board.getPiece(newPosition)==null;
  }
  public static boolean validateTakeTeamPiece(PieceMoves pieceMoves, ChessPosition newPosition){
    return pieceMoves.board.getPiece(newPosition).getTeamColor() != pieceMoves.piece.getTeamColor();
  }
  /**
   *
   * @param pieceMoves current PieceMoves Object
   * @param newPosition new position
   * @return true if it's at its opponent's edge
   */
  public static boolean validatePromotionRule(PieceMoves pieceMoves, ChessPosition newPosition){
    var whiteAtEdge = pieceMoves.piece.getTeamColor() == ChessGame.TeamColor.WHITE
            && newPosition.getRow() ==8;
    var blackAtEdge = pieceMoves.piece.getTeamColor() == ChessGame.TeamColor.BLACK
            && newPosition.getRow() ==1;
    return whiteAtEdge || blackAtEdge;
  }

  /**
   * @param pieceMoves Current PieceMoves object
   * @return if piece can move 2 steps
   */
  public static boolean validateInitialMove(PieceMoves pieceMoves){
    var whiteInitialPosition= pieceMoves.piece.getTeamColor() == ChessGame.TeamColor.WHITE &&
            pieceMoves.myPosition.getRow() ==2;
    var blackInitialPosition = pieceMoves.piece.getTeamColor() == ChessGame.TeamColor.BLACK &&
            pieceMoves.myPosition.getRow() ==7;
    return whiteInitialPosition || blackInitialPosition;
  }
  /**
   *
   * @param pieceMoves Current PieceMoves object
   * @param newPosition new Position
   * @return true if piece can take opponent's piece
   */
  public static boolean validatePawnTakePiece(PieceMoves pieceMoves, ChessPosition newPosition){
    if (newPosition.getColumn() != pieceMoves.myPosition.getColumn()){
      return !validateEmptyPosition(pieceMoves, newPosition)
              && validateTakeTeamPiece(pieceMoves,newPosition);
    }
    return false;
  }
}
