package chess;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final Dictionary<ChessPosition, ChessPiece> piecePosition = new Hashtable<>();
    public ChessBoard() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(piecePosition, that.piecePosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piecePosition);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        piecePosition.put(position,piece);
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return piecePosition.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        int rowWhiteA = 1;
        int rowWhiteB = 2;
        int rowBlackA = 8;
        int rowBlackB = 7;
        //White Pieces
        //KINK
        addPiece(new ChessPosition(rowWhiteA,5),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        //Queen
        addPiece(new ChessPosition(rowWhiteA,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));

        //Bishops
        addPiece(new ChessPosition(rowWhiteA,3),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(rowWhiteA,6),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        //KNIGHTS
        addPiece(new ChessPosition(rowWhiteA,2),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(rowWhiteA,7),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));

        //Rooks
        addPiece(new ChessPosition(rowWhiteA,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(rowWhiteA,8),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        //PAWNS
        for (int i=1;i <=8; i++){
            addPiece(new ChessPosition(rowWhiteB,i),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }

        //Black Pieces
        //KINK
        addPiece(new ChessPosition(rowBlackA,5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        //Queen
        addPiece(new ChessPosition(rowBlackA,4),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));

        //Bishops
        addPiece(new ChessPosition(rowBlackA,3),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(rowBlackA,6),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        //KNIGHTS
        addPiece(new ChessPosition(rowBlackA,2),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(rowBlackA,7),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));

        //Rooks
        addPiece(new ChessPosition(rowBlackA,1),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(rowBlackA,8),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        for (int i=1;i <=8; i++){
            addPiece(new ChessPosition(rowBlackB,i),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
    }
}
