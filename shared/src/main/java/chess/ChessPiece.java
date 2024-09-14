package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleChessMoves = new HashSet<>();
        if (this.type == PieceType.PAWN) {
            possibleChessMoves = new PawnMoves(board, myPosition, pieceColor).getMoves();
        }
        else if (this.type == PieceType.KING){
            possibleChessMoves = new KingMoves(board,myPosition,pieceColor).getMoves();
        }else if (this.type == PieceType.QUEEN){
            possibleChessMoves = new QueenMoves(board,myPosition,pieceColor).getMoves();
        }else if (this.type == PieceType.BISHOP){
            possibleChessMoves = new BishopMoves(board,myPosition,pieceColor).getMoves();
        }
        else if (this.type== PieceType.KNIGHT) {
            possibleChessMoves = new KnightMoves(board, myPosition, pieceColor).getMoves();
        }

        else if (this.type == PieceType.ROOK){
            possibleChessMoves = new RookMoves(board,myPosition,pieceColor).getMoves();
        }
        return possibleChessMoves;
    }

}
