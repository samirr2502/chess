package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;

public class PieceMovesAbs {
  ArrayList<ChessMove> possibleMoves = new ArrayList<>();
  ChessBoard board;
  ChessPiece piece;
  ChessPosition myPosition;

  public ArrayList<ChessMove> getPossibleMoves(){
    return possibleMoves;
  }
  void addMove(ChessMove move){
    possibleMoves.add(move);
  }
  boolean validateMoves(ChessPosition newPosition){
    var insideRows = newPosition.getRow()<=8 && newPosition.getRow()>=1;
    var insideColumns = newPosition.getColumn()<=8 && newPosition.getColumn()>=1;
    var noTeamPiece = false;
    if( insideRows && insideColumns) {
      noTeamPiece = board.getPiece(newPosition) == null ||
              board.getPiece(newPosition).getTeamColor() != piece.getTeamColor();
      return noTeamPiece;
    }
    return false;
  }
  boolean validatePromotionRule(ChessPosition newPosition){
    var whiteEnd = newPosition.getRow()==8 && piece.getTeamColor() == ChessGame.TeamColor.WHITE;
    var blackEnd = newPosition.getRow()==1 && piece.getTeamColor() == ChessGame.TeamColor.BLACK;
    return whiteEnd || blackEnd;
  }
  boolean validateMovesRule_3_0(ChessPosition newPosition){
    var insideRows = newPosition.getRow()<=8 && newPosition.getRow()>=1;
    var insideColumns = newPosition.getColumn()<=8 && newPosition.getColumn()>=1;
    var noPiece = false;
    if( insideRows && insideColumns) {
      noPiece = board.getPiece(newPosition) == null;
      return noPiece;
    }
    return false;
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
  int incrementRowCol(int rowOrCol){
    if (rowOrCol !=0) {
      if(rowOrCol >0) {
        return +1;
      } else {
        return -1;
      }
    }
    return 0;
  }
  void promotePiece(ChessPosition newPosition){
      possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
      possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
      possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
      possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));

  }
  void moveRule_3(int cont, int moveRow, int moveCol, ChessPiece.PieceType promoPiece){
    ChessPosition newPosition = new ChessPosition(myPosition.getRow() + moveRow, myPosition.getColumn() + moveCol);
    if (newPosition.getColumn() == myPosition.getColumn()) {
      //Check if piece has valid moves
      if (validateMovesRule_3_0(newPosition)) {
        if(validatePromotionRule(newPosition)){
          promotePiece(newPosition);
        }else {
          possibleMoves.add(new ChessMove(myPosition, newPosition, promoPiece));
        }
      }
      //Check if piece can move twice in first move
      ChessPosition newPosition2 = new ChessPosition(myPosition.getRow() +(moveRow*2), myPosition.getColumn() + moveCol);
      if (validateMovesRule_3_1() && validateMovesRule_3_0(newPosition2)&& validateMoves(newPosition)) {
        possibleMoves.add(new ChessMove(myPosition, newPosition2, promoPiece));
      }
    } else { //Check if piece can eat
      if (validateMovesRule_3_2(newPosition)){
        if(validatePromotionRule(newPosition)){
          promotePiece(newPosition);
        }else {
          possibleMoves.add(new ChessMove(myPosition, newPosition, promoPiece));
        }
      }
    }
  }
  void moveRule_2(int cont, int moveRow, int moveCol, ChessPiece.PieceType promoPiece){
    //int edge = rowEdge == 0 ? colEdge: rowEdge;
    for (int i=0; i<8; i++) {
      ChessPosition newPosition = new ChessPosition(myPosition.getRow() + moveRow, myPosition.getColumn() + moveCol);
      if (validateMoves(newPosition)) {
        possibleMoves.add(new ChessMove(myPosition, newPosition, promoPiece));
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

  void moveRule_1(int cont, int moveRow, int moveCol, ChessPiece.PieceType promoPiece){
      ChessPosition newPosition = new ChessPosition(myPosition.getRow() + moveRow, myPosition.getColumn() + moveCol);
      if (validateMoves(newPosition)) {
        possibleMoves.add(new ChessMove(myPosition, newPosition, promoPiece));
    }
  }
  void setMoves(int cont, int moveRow, int moveCol, ChessPiece.PieceType promoPiece){
    //int edge = rowEdge == 0 ? colEdge: rowEdge;
    switch (cont){
      case 1:
        moveRule_1(cont, moveRow,moveCol,promoPiece);
        break;
      case 2:
        moveRule_2(cont, moveRow,moveCol,promoPiece);
        break;
      case 3:
        moveRule_3(cont, moveRow,moveCol,promoPiece);
        break;
    }
  }
  void setDirection(int cont, ChessPiece.PieceType promoPiece) {

    //Move all directions
    setMoves(cont, 0, 1,promoPiece);//Move right
    setMoves(cont, 1, 1,promoPiece);//Move topRight
    setMoves(cont, 1, 0,promoPiece);//Move top
    setMoves(cont, 1, -1,promoPiece);//Move topLeft
    setMoves(cont, 0, -1,promoPiece);//Move left
    setMoves(cont, -1, -1,promoPiece);//Move bottomLeft
    setMoves(cont, -1, 0,promoPiece);//Move bottom
    setMoves(cont, -1, 1,promoPiece);//Move bottomRight
  }

}
