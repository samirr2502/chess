package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static java.lang.Math.abs;
import static ui.EscapeSequences.*;

public class DrawnBoard {
  // Board dimensions.
  private static final int BOARD_SIZE_IN_SQUARES = 10;
  private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
  private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

  // Padded characters.
  private static final String EMPTY = "  ";


  public static void run(ChessGame.TeamColor teamColor, ChessBoard board) {
    board.resetBoard();

    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    out.print(ERASE_SCREEN);

    switch (teamColor) {
      case WHITE -> {
        drawHeaders(out, ChessGame.TeamColor.WHITE);
        drawChessBoard(out, ChessGame.TeamColor.WHITE, board);
        drawHeaders(out, ChessGame.TeamColor.WHITE);
      }
      case BLACK -> {
        drawHeaders(out, ChessGame.TeamColor.BLACK);
        drawChessBoard(out, ChessGame.TeamColor.BLACK, board);
        drawHeaders(out, ChessGame.TeamColor.BLACK);
      }
    }
  }

  private static void drawHeaders(PrintStream out, ChessGame.TeamColor color) {

    String[] headersWhite = {"  ", "a", "b", "c", "d", "e", "f", "g", "h", "  "};
    String[] headersBlack = {"  ", "h", "g", "f", "e", "d", "c", "b", "a", "  "};
    String[] headers = color == ChessGame.TeamColor.WHITE ? headersWhite : headersBlack;
    for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      drawHeader(out, headers[boardCol]);
      if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
        out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
      }
    }
    out.print(RESET_BG_COLOR);
    out.println();
  }

  private static void drawHeader(PrintStream out, String headerText) {
    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

    out.print(EMPTY.repeat(prefixLength));
    printHeaderText(out, headerText);
    out.print(EMPTY.repeat(suffixLength));
  }

  private static void printHeaderText(PrintStream out, String player) {
    out.print(SET_BG_COLOR_DARK_GREEN);
    out.print(SET_TEXT_COLOR_BLACK);
    out.print(player);
  }

  private static void drawChessBoard(PrintStream out, ChessGame.TeamColor teamColor, ChessBoard board) {
    int offset = (teamColor == ChessGame.TeamColor.WHITE) ? 7 : 0;
    int rows = 0;
    int cols = 7;
    int r2;
    int c2;
    for (int r = 8; r > rows; r--) {
      r2 = abs(8 - (r + offset));
      out.print(SET_BG_COLOR_DARK_GREEN);
      out.print(SET_TEXT_COLOR_BLACK);
      out.printf(" %d ", r2 + 1);
      for (int c = 0; c <= cols; c++) {
        c2 = abs(7 - (c + offset));
        out.print(SET_TEXT_COLOR_WHITE);
        ChessPosition pos = new ChessPosition(r2 + 1, c2 + 1);
        if ((c + r) % 2 != 0) {
          out.print(SET_BG_COLOR_DARK_GREY);
        } else {
          out.print(SET_BG_COLOR_LIGHT_GREY);
        }
        if (board.getPiece(pos) == null) {
          out.print("   ");
        } else {
          addPieces(out, board, pos);
        }
      }
      out.print(SET_TEXT_COLOR_BLACK);

      out.print(SET_BG_COLOR_DARK_GREEN);
      out.printf(" %d ", r2 + 1);
      out.print(RESET_BG_COLOR);
      out.println();
    }
  }

  private static void addPieces(PrintStream out, ChessBoard board, ChessPosition pos) {
    ChessGame.TeamColor color = board.getPiece(pos).getTeamColor();
    ChessPiece.PieceType type = board.getPiece(pos).getPieceType();
    if (color == ChessGame.TeamColor.WHITE) {
      switch (type) {
        case QUEEN -> out.print(WHITE_QUEEN);
        case KING -> out.print(WHITE_KING);
        case KNIGHT -> out.print(WHITE_KNIGHT);
        case ROOK -> out.print(WHITE_ROOK);
        case BISHOP -> out.print(WHITE_BISHOP);
        case PAWN -> out.print(WHITE_PAWN);
      }
    } else {
      switch (type) {
        case QUEEN -> out.print(BLACK_QUEEN);
        case KING -> out.print(BLACK_KING);
        case KNIGHT -> out.print(BLACK_KNIGHT);
        case ROOK -> out.print(BLACK_ROOK);
        case BISHOP -> out.print(BLACK_BISHOP);
        case PAWN -> out.print(BLACK_PAWN);
      }
    }
  }
}