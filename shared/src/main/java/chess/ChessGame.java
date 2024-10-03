package chess;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamColor = TeamColor.WHITE;
    ChessBoard board;
    Collection<ChessMove> chessMoves = new ArrayList<>();
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        chessMoves = (piece ==null)? null : piece.pieceMoves(board,startPosition);
        assert chessMoves != null;
        ChessBoard testBoard = board;
        for (var move : chessMoves){
            for (int i = 1;i<=8;i++){
                for (int j = 1; j<=8 ; j++){
                    ChessPiece opponentPiece = testBoard.board[i][j] ==null? null : testBoard.board[i][j];
                    if (opponentPiece!= null
                            && testBoard.board[i][j].getTeamColor() != getTeamTurn() ){
                        ChessPosition opponentPosition = new ChessPosition(i,j);
                        opponentPiece.pieceMoves(testBoard,opponentPosition);
                    }
                }
            }
        }
        return chessMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
      return board != null && !checkPieces(teamColor).isEmpty();
    }

    public ArrayList<ChessPiece> checkPieces(TeamColor teamColor){
        ArrayList<ChessPiece> checkPieces= new ArrayList<>();
        ChessPosition kingPosition = getTeamsKingPosition(teamColor);
        for (int i = 0;i<8;i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece opponentPiece = board.board[i][j] == null ? null : board.board[i][j];
                if (opponentPiece != null
                        && board.board[i][j].getTeamColor() != teamColor) {
                    ChessPosition opponentPosition = new ChessPosition(i+1, j+1);
                    Collection<ChessMove> opponentMoves = opponentPiece.pieceMoves(board, opponentPosition);
                    for (ChessMove move : opponentMoves) {
                        if (move.getEndPosition().getColumn() == kingPosition.getColumn()
                                && move.getEndPosition().getRow() == kingPosition.getRow()) {
                            checkPieces.add(opponentPiece);
                        }
                    }
                }
            }
        }
        return checkPieces;
    }
    public ChessPosition getTeamsKingPosition(TeamColor teamColor){
        if (this.board != null) {
            for (int i = 0;i<=7;i++) {
                for (int j = 0; j <= 7; j++) {
                    if (this.board.board[i][j] !=null &&
                            this.board.board[i][j].getPieceType() == ChessPiece.PieceType.KING &&
                            this.board.board[i][j].getTeamColor() == teamColor
                    ) {
                        return new ChessPosition(i+1,j+1);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor) &&board!=null) {
            ChessPosition kingPosition = getTeamsKingPosition(teamColor);
            ChessPiece kingPiece = board.getPiece(kingPosition);
            ArrayList<ChessPiece> checkPieces= checkPieces(teamColor);
            Collection<ChessMove> kingMoves= kingPiece.pieceMoves(board,kingPosition);
            int totalKingsMove = kingMoves.size();
            int counter=0;
            ChessBoard testBoard= board.clone();;
            boolean newPositionInCheck = false;
            for (var move:kingMoves){

                testBoard.board[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1] = null;
                testBoard.board[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] = kingPiece;
                for (int i = 0;i<8;i++) {
                    for (int j = 0; j < 8; j++) {
                        //Get piece at position
                        ChessPiece opponentPiece = testBoard.board[i][j];
                        //If position is not empty (if there's a piece) and is not the current team's piece
                        if (opponentPiece != null
                                && testBoard.board[i][j].getTeamColor() != teamColor) {
                            //create position
                            ChessPosition opponentPosition = new ChessPosition(i + 1, j + 1);
                            //get moves
                            Collection<ChessMove> opponentMoves = opponentPiece.pieceMoves(testBoard, opponentPosition);
                            //check if any of those moves matches the new king's position
                            for (ChessMove opponentMove : opponentMoves) {
                                if (opponentMove.getEndPosition().getColumn() == move.getEndPosition().getColumn()
                                        && opponentMove.getEndPosition().getRow() == move.getEndPosition().getRow()) {
                                    newPositionInCheck =true;
                                    testBoard.board[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] = null;
                                    break;
                                }
                            }
                        } if (newPositionInCheck){
                            break;
                        }
                    } if (newPositionInCheck){
                        break;
                    }
                }
                if(newPositionInCheck) {
                    counter += 1;
                }

                newPositionInCheck = false;

            }

            return totalKingsMove == counter;

        }
        return false;
    }
    boolean kingCantMove(){
        return true;
    }
    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (board!=null) {
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
            this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        if (this.board==null) {
            this.board = new ChessBoard();
            this.board.resetBoard();
            return this.board;
        }
        return this.board;
    }
}
