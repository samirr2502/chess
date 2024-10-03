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
        if(board !=null) {
            ChessBoard testBoard = board.clone();
            ChessPiece myPiece = testBoard.getPiece(startPosition);
            Collection<ChessMove> moves = myPiece.pieceMoves(testBoard, startPosition);
            Collection<ChessMove> validMoves = new ArrayList<>();
            assert chessMoves != null;
            for (var move : moves) {
                ChessPiece savedPiece = testBoard.board[move.getEndPosition().getRow() - 1][move.getEndPosition().getColumn() - 1];
                testBoard.board[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1] = null;
                testBoard.board[move.getEndPosition().getRow() - 1][move.getEndPosition().getColumn() - 1] = myPiece;
                Collection<ChessPiece> inCheckPieces = inCheckPieces(myPiece.getTeamColor(), testBoard);
                if (inCheckPieces.isEmpty()) {
                    validMoves.add(move);
                }
                testBoard.board[move.getEndPosition().getRow() - 1][move.getEndPosition().getColumn() - 1] = savedPiece;
                testBoard.board[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1] = myPiece;

            }
            return validMoves;
        }
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves= validMoves(move.getStartPosition());

        if (validMoves !=null && validMoves.contains(move)){
            ChessPiece myPiece = board.getPiece(move.getStartPosition());
            if (move.getPromotionPiece()!=null) {
                myPiece= new ChessPiece(myPiece.getTeamColor(),move.getPromotionPiece());
            }
            board.board[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1] = null;
            board.board[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] = myPiece;
        } else{
            throw new InvalidMoveException("Can't make that move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return board != null && !inCheckPieces(teamColor, this.board).isEmpty();
    }

    public ArrayList<ChessPiece> inCheckPieces(TeamColor teamColor, ChessBoard testBoard) {
        ArrayList<ChessPiece> checkPieces = new ArrayList<>();
        ChessPosition kingPosition = getTeamsKingPosition(teamColor, testBoard);
        if (kingPosition ==null){
            return checkPieces;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece opponentPiece = testBoard.board[i][j] == null ? null : testBoard.board[i][j];
                if (opponentPiece != null
                        && testBoard.board[i][j].getTeamColor() != teamColor) {
                    ChessPosition opponentPosition = new ChessPosition(i + 1, j + 1);
                    Collection<ChessMove> opponentMoves = opponentPiece.pieceMoves(testBoard, opponentPosition);
                    for (ChessMove move : opponentMoves) {
                        if (move.getEndPosition().getColumn() == kingPosition.getColumn()
                                && move.getEndPosition().getRow() == kingPosition.getRow()) {
                            opponentPiece.myPosition = opponentPosition;
                            if(!checkPieces.contains(opponentPiece)){
                                checkPieces.add(opponentPiece);
                            }
                        }
                    }
                }
            }
        }
        return checkPieces;
    }

    public ChessPosition getTeamsKingPosition(TeamColor teamColor, ChessBoard testBoard) {
        if (testBoard != null) {
            for (int i = 0; i <= 7; i++) {
                for (int j = 0; j <= 7; j++) {
                    if (testBoard.board[i][j] != null &&
                            testBoard.board[i][j].getPieceType() == ChessPiece.PieceType.KING &&
                            testBoard.board[i][j].getTeamColor() == teamColor
                    ) {
                        return new ChessPosition(i + 1, j + 1);
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

        ChessBoard testBoard1 = (board==null)? null:board.clone();
        return (board!=null
                && isInCheck(teamColor)
                && kingCantMove(teamColor,testBoard1)
                && teamCantMovePiece(teamColor,testBoard1));
    }
    public boolean teamCantMovePiece(TeamColor teamColor, ChessBoard testBoard){
        testBoard = null;
        testBoard = board.clone();
        ArrayList<ChessPiece> inCheckPieces= inCheckPieces(teamColor,testBoard);
        if (inCheckPieces.size() >1){
            return true;
        }
        for (var opponentsPiece:inCheckPieces){
            for (int i=0;i<8; i++){
                for (int j=0;j<8;j++){
                    ChessPiece myPiece = testBoard.board[i][j];
                    if(myPiece!=null && myPiece.getTeamColor() ==teamColor){
                        ChessPosition myPosition = new ChessPosition(i+1,j+1);
                        Collection<ChessMove> myMoves = myPiece.pieceMoves(testBoard,myPosition);
                        for (var move:myMoves){
                            if(move.getEndPosition().getRow()-1 == opponentsPiece.myPosition.getRow()-1
                            &&move.getEndPosition().getColumn()-1 == opponentsPiece.myPosition.getColumn()-1){
                                testBoard.board[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1] = null;
                                testBoard.board[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] = myPiece;
                                return (!inCheckPieces(teamColor, testBoard).isEmpty() && !kingCantMove(teamColor,testBoard));

                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    public boolean kingCantMove(TeamColor teamColor, ChessBoard testBoard){
            ChessPosition kingPosition = getTeamsKingPosition(teamColor, testBoard);
            ChessPiece kingPiece = testBoard.getPiece(kingPosition);
            Collection<ChessMove> kingMoves= kingPiece.pieceMoves(testBoard,kingPosition);
            int totalKingsMove = kingMoves.size();
            int counter=0;
            boolean newPositionInCheck = false;
            for (var move:kingMoves){
                ChessPiece savedPiece = testBoard.board[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1];
                ChessPosition savedPosition = new ChessPosition(move.getEndPosition().getRow()-1,move.getEndPosition().getColumn()-1);
                testBoard.board[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1] = null;
                testBoard.board[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] = kingPiece;
                for (int i = 0;i<8;i++) {
                    for (int j = 0; j < 8; j++) {
                        //Get piece at position
                        ChessPiece opponentPiece = testBoard.board[i][j];
                        //If position is not empty (if there's a piece) and is not the current team's piece
                        if (opponentPiece != null
                                && opponentPiece.getTeamColor() != teamColor) {
                            //create position
                            ChessPosition opponentPosition = new ChessPosition(i + 1, j + 1);
                            //get moves
                            Collection<ChessMove> opponentMoves = opponentPiece.pieceMoves(testBoard, opponentPosition);
                            //check if any of those moves matches the new king's position
                            for (ChessMove opponentMove : opponentMoves) {
                                if (opponentMove.getEndPosition().getColumn() == move.getEndPosition().getColumn()
                                        && opponentMove.getEndPosition().getRow() == move.getEndPosition().getRow()) {
                                    newPositionInCheck =true;
                                    testBoard.board[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] = savedPiece;
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
            testBoard.board[kingPosition.getRow()-1][kingPosition.getColumn()-1] = kingPiece;
            return totalKingsMove == counter;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(board!=null) {
            return kingCantMove(teamColor, board);
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
