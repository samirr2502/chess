package clients;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import ui.DrawnBoard;
import ui.Repl;
import ui.ServerFacade;
import ui.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static java.lang.Integer.parseInt;

public class InGameClient implements ChessClient{
  private final ServerFacade server;
  private Collection<ChessMove> validMoves = new ArrayList<>();

  public InGameClient(String serverUrl){
   server = new ServerFacade(serverUrl);
  }

  @Override
  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "board" -> getBoard(params);
        case "moves" -> getMoves(params);
        case "move" -> makeMove(params);
        case "leave" -> leaveGame(params);
        case "resign" -> resignGame(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (Exception ex) {
      return ex.getMessage();
    }
  }
  private String getBoard(String[] params) throws Exception {
    if(params.length==0) {
      Repl.chessBoard.resetBoard();
      return drawBoard();
    }
    throw new Exception("Expected: board");
  }
  private String getMoves(String[] params) throws Exception{
    if(params.length==1){

      int row = parseInt(params[0]);
      int col = parseInt(params[0]);
      validMoves= Repl.currentGameData.game().validMoves(new ChessPosition(row -1, col-1));
      highLightValidMoves(validMoves);
    }
    return "";
  }

  private void highLightValidMoves(Collection<ChessMove> validMoves) {
    DrawnBoard.run(Repl.lastJoinedGameColor,Repl.chessBoard,validMoves);
    validMoves.clear();
  }

  private String makeMove(String[] params) {
    return "";
  }



  private String leaveGame(String[] params) throws Exception {
    if(params.length==0) {
      switch (Repl.lastJoinedGameColor) {
        case WHITE ->Repl.currentGame.whiteUsername = null;
        case BLACK -> Repl.currentGame.blackUsername =null;
      }
      Repl.state = State.LOGGED_IN;
      return "You left the Game \n\n Type help for commands";
    }
    throw new Exception("Expected: leave");
  }
  private String resignGame(String[] params) {
    return "";
  }
  private String drawBoard(){
     DrawnBoard.run(ChessGame.TeamColor.WHITE,Repl.chessBoard,validMoves);
     System.out.println();
     DrawnBoard.run(ChessGame.TeamColor.BLACK,Repl.chessBoard, validMoves);
     return "";

  }
  @Override
  public String help() {
    return """
            Commands:
               - board - redraw current board
               - move <Start Position> <End Position> - Move piece
               - moves <Piece Position> - highlight legal moves
               - leave - leave game
               - resign - give up
               - quit - exit program
               - help""";
  }
}
