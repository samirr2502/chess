package chess;

import chess.PieceMoves.*;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return 31*Objects.hash(color, type);
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

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
         return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessMoves = new ArrayList<>();
        switch (getPieceType()){
            case KING -> chessMoves = new KingMoves(board,this, myPosition).getPossibleMoves();
            case QUEEN -> chessMoves= new QueenMoves(board, this,myPosition).getPossibleMoves();
            case BISHOP -> chessMoves= new BishopMoves(board, this,myPosition).getPossibleMoves();
            case ROOK -> chessMoves= new RookMoves(board, this,myPosition).getPossibleMoves();
            case KNIGHT -> chessMoves= new KnightMoves(board, this,myPosition).getPossibleMoves();
            case PAWN -> chessMoves= new PawnMoves(board, this,myPosition).getPossibleMoves();
        }
        return chessMoves;
    }
}
