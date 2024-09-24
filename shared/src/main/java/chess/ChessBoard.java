package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board = new ChessPiece[10][10];
    private final Map<Character, ChessPiece.PieceType> pieceTypeMap= new HashMap<> (){{
        put('p', ChessPiece.PieceType.PAWN);
        put('k', ChessPiece.PieceType.KING);
        put('q', ChessPiece.PieceType.QUEEN);
        put('n', ChessPiece.PieceType.KNIGHT);
        put('b', ChessPiece.PieceType.BISHOP);
        put('r', ChessPiece.PieceType.ROOK);
        }
    };
    public ChessBoard() {
        
    }
    /**
     * Prints the board with the pieces
     */

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board) && Objects.equals(pieceTypeMap, that.pieceTypeMap);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(pieceTypeMap);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        int row = 8;
        int column = 1;
        String newBoard = """
               |r|n|b|q|k|b|n|r|
               |p|p|p|p|p|p|p|p|
               | | | | | | | | |
               | | | | | | | | |
               | | | | | | | | |
               | | | | | | | | |
               |P|P|P|P|P|P|P|P|
               |R|N|B|Q|K|B|N|R|
                """;
        for (char c: newBoard.toCharArray()){
            switch (c){
                case '|':
                    continue;
                case '\n':
                    column =1;
                    row--;
                    break;
                case ' ':
                    column++;
                    break;
                default:
                    ChessGame.TeamColor teamColor;
                    if (Character.isLowerCase(c)) {
                        teamColor = ChessGame.TeamColor.BLACK;
                    } else{
                        teamColor = ChessGame.TeamColor.WHITE;
                    }
                ChessPosition position = new ChessPosition(row,column);
                ChessPiece piece = new ChessPiece(teamColor, pieceTypeMap.get(Character.toLowerCase(c)));
                addPiece(position,piece);
                column ++;
            }
        }
    }

}
