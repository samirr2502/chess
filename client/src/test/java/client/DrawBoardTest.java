package client;

import chess.ChessBoard;
import chess.ChessGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.DrawnBoard;




public class DrawBoardTest {
  ChessBoard board;
  @BeforeEach
  public void setUp(){
    board = new ChessBoard();
    board.resetBoard();
  }
  @Test
  public void printDrawBoard() {
      DrawnBoard.run(ChessGame.TeamColor.WHITE, board);
      System.out.println();
      DrawnBoard.run(ChessGame.TeamColor.BLACK, board);

  }
  @Test
  public void printBoard(){
   // DrawnBoard.run(ChessGame.TeamColor.WHITE, new ChessBoard());
  }
}
