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
            possibleChessMoves = pawnMoves(board, myPosition);
        }
        return possibleChessMoves;
        }

    public void pawnMoves(Collection<ChessMove> possibleChessMoves,
            ChessBoard board, ChessPosition myPosition,
                         ChessPosition left, ChessPosition right, ChessPiece.PieceType promotionPiece){
        if (myPosition.getColumn() != 1 && board.getPiece(right) != null){
            possibleChessMoves.add(new ChessMove(myPosition,right,promotionPiece));
        }
        if (myPosition.getColumn() != 8 && board.getPiece(left) != null){
            possibleChessMoves.add(new ChessMove(myPosition,left,promotionPiece));
        }
    }
    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleChessMoves = new HashSet<>();
        boolean canPromote = false;

        //WHITE MOVES
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            //move to front
            ChessPosition moveForwardWhite = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            ChessPosition rightWhite = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            ChessPosition leftWhite = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);

            if (myPosition.getRow() == 7) {
                canPromote = true;
            }
            pawnMoves(possibleChessMoves, board, myPosition, leftWhite, rightWhite, null);

        } else {
            //Black Moves
            //move to front
            ChessPosition moveForwardBlack = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            ChessPosition rightBlack = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            ChessPosition leftBlack = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if (myPosition.getRow() == 2) {
                canPromote = true;
            }
            pawnMoves(possibleChessMoves, board, myPosition, leftBlack, rightBlack, null);
        }
        return possibleChessMoves;
    }

}
