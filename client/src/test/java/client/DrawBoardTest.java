package client;

import chess.ChessBoard;
import chess.ChessGame;

import chess.ChessMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.DrawnBoard;

import java.util.Collection;


public class DrawBoardTest {
  ChessBoard board;
  Collection<ChessMove> validMoves;
  @BeforeEach
  public void setUp(){
    board = new ChessBoard();
    board.resetBoard();
  }
  @Test
  public void printDrawBoard() {
      DrawnBoard.run(ChessGame.TeamColor.WHITE, board, validMoves);
      System.out.println();
      DrawnBoard.run(ChessGame.TeamColor.BLACK, board, validMoves);

  }
}
