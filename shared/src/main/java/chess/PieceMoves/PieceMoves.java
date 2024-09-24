package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;

import static chess.PieceMoves.PieceMovesRules.*;

/**
 * PieceMoves abstract class
 * Contains:
 * Attributes:
 *    -> possibleMoves, board, myPosition,piece
 * SetMoves() Handles the rule cases
 * SetDirection() It's overridden by inheritance classes for implementation
 */
public abstract class PieceMoves {
  ArrayList<ChessMove> possibleMoves = new ArrayList<>(); //Local
  ChessBoard board; //Ask for it in extended class constructor
  ChessPiece piece; //Ask for it in extended class constructor
  ChessPosition myPosition; //Ask for it in extended class constructor

  /**
   * Gets the Array of possible moves
   * Override in each class and add Set moves so this can be called from ChessPiece class
   * @return list of possible moves
   */
  public ArrayList<ChessMove> getPossibleMoves(){
    return possibleMoves;
  }


  /**
   * Sets the cases for what rule to use
   * @param rule The Rule number code
   * @param moveRow The Direction is going vertically
   * @param moveCol the Direction is going horizontally
   */
  void setMoves(int rule, int moveRow, int moveCol){
    switch (rule){
      case 1:
        movesRule_1(this,moveRow,moveCol);
        break;
      case 2:
        movesRule_2(this,moveRow,moveCol);
        break;
      case 3:
        movesRule_3(this,moveRow,moveCol);
        break;
    }
  }
  /**
   * Sets the directions
   * Calls set Moves with the possible directions a piece could go
   * Starts with all directions that work for King and Queen
   * Each Class that extends should override this function to provide its own set of directions
   * @param rule a number code for the Rule that each direction is going to follow in the setMoves() function
   */
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
