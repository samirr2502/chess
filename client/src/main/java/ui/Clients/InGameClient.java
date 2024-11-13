package ui.Clients;

import chess.ChessBoard;
import chess.ChessGame;
import ui.DrawnBoard;
import ui.Repl;
import ui.ServerFacade;
import ui.State;

import java.util.Arrays;

public class InGameClient implements ChessClient{
  private final ServerFacade server;

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
        case "surrender" -> surrender(params);
        case "leave" -> leaveGame(params);
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
      return drawBoard(params);
    }
    throw new Exception("Expected: <board>");
  }
  private String surrender(String[] params) {
    return "not implemented yet";
  }
  private String leaveGame(String[] params) {
    Repl.state=State.LOGGED_IN;
    return "You left the Game \n\n Type help for commands";
  }
  private String drawBoard(String[] params){
     DrawnBoard.run(ChessGame.TeamColor.WHITE,Repl.chessBoard);
     System.out.println();
     DrawnBoard.run(ChessGame.TeamColor.BLACK,Repl.chessBoard);
     return "";

  }
  @Override
  public String help() {
    return """
            Commands:
            - board - show current board
            - surrender - admit defeat
            - leave - leave game
            - quit
            - help""";
  }
}
