package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.abs;
import static ui.EscapeSequences.*;

public class DrawnBoard {
  // Board dimensions.
  private static final int BOARD_SIZE_IN_SQUARES = 10;
  private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
  private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

  // Padded characters.
  private static final String EMPTY = "  ";


  public static void run(ChessGame.TeamColor teamColor, ChessBoard board, Collection<ChessMove> validMoves) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    out.print(ERASE_SCREEN);

    switch (teamColor) {
      case WHITE -> {
        drawHeaders(out, ChessGame.TeamColor.WHITE);
        drawChessBoard(out, ChessGame.TeamColor.WHITE, board,validMoves);
        drawHeaders(out, ChessGame.TeamColor.WHITE);
      }
      case BLACK -> {
        drawHeaders(out, ChessGame.TeamColor.BLACK);
        drawChessBoard(out, ChessGame.TeamColor.BLACK, board, validMoves);
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

  private static void drawChessBoard(PrintStream out, ChessGame.TeamColor teamColor, ChessBoard board, Collection<ChessMove> validMoves) {
    int offset = (teamColor == ChessGame.TeamColor.WHITE) ? 7 : 0;
    int rows = 0;
    int cols = 7;
    int r2;
    int c2;
    Collection<ChessPosition> positions = new ArrayList<>();
    if (validMoves != null){
    for (ChessMove move: validMoves){
      ChessMove newMove = new ChessMove(new ChessPosition(0,0),
              new ChessPosition(move.getEndPosition().getRow()-1, move.getEndPosition().getColumn()-1), null);
      positions.add(newMove.getEndPosition());
    }
    }
    for (int r = 8; r > rows; r--) {
      r2 = abs(8 - (r + offset));
      out.print(SET_BG_COLOR_DARK_GREEN);
      out.print(SET_TEXT_COLOR_BLACK);
      out.printf(" %d ", r2 + 1);
      for (int c = 0; c <= cols; c++) {
        c2 = abs(7 - (c + offset));
        ChessPosition position = new ChessPosition(r2,c2);

        out.print(SET_TEXT_COLOR_WHITE);
        ChessPosition pos = new ChessPosition(r2 + 1, c2 + 1);
        if ((c + r) % 2 != 0) {
          if (positions.contains(position)){
            out.print(SET_BG_COLOR_DARK_GREEN);
          } else {
            out.print(SET_BG_COLOR_DARK_GREY);
          }
        } else {
          if(positions.contains(position)){
            out.print(SET_BG_COLOR_OPAQUE_GREEN);
          } else {
            out.print(SET_BG_COLOR_LIGHT_GREY);
          }
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