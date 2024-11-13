package client;

import chess.ChessPosition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static ui.EscapeSequences.*;

public class DrawBoardTest {
  @Test
  public void printBoard(){
//    Map<String, ChessPosition> pieces = null;
//    List<String> orderedPieces = List.of(WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,
//                                WHITE_QUEEN, WHITE_KING,
//                                WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK);
//    int index=0;
//    for (String piece: orderedPieces){
//      pieces.put(piece, new ChessPosition(0, index));
//      index++;
//    }
//    for (int i=0;i<8;i++){
//      pieces.put(WHITE_PAWN, new ChessPosition(1,i));
//    }
//    String white_pieces="";
//    for (Map.Entry<String, ChessPosition> entry : pieces.entrySet()){
//      white_pieces = white_pieces.concat(entry.getKey());
//    }


    String board = String.format("""  
              1 2 3 4 5 6 7 8
            h%s%s%s%s%s%s%s%sh
            g 0 0 0 0 0 0 0 0 g
            f 0 0 0 0 0 0 0 0 f
            e 0 0 0 0 0 0 0 0 e
            d 0 0 0 0 0 0 0 0 d
            c 0 0 0 0 0 0 0 0 c
            b X X X X X X X X b
            a Y Y Y Y Y Y Y Y a
              1 2 3 4 5 6 7 8
            """,
    WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN)
    ;
    System.out.println(board);
  }
}
