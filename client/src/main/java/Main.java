import chess.*;
import ui.Repl;

public class Main {
    public final static int PORT = 50252;

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var serverUrl = "http://localhost:" + PORT;
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new Repl(serverUrl).run();
    }
}