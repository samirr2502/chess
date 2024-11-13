package ui.Clients;

import ui.Repl;
import ui.ServerFacade;
import ui.State;

import java.util.Arrays;

import static ui.EscapeSequences.*;

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

  private String getBoard(String[] params) {
    return drawBoardWhite(params);
  }
  private String surrender(String[] params) {
    return "";
  }
  private String leaveGame(String[] params) {
    Repl.state=State.LOGGED_IN;
    return "You left the Game out \n\n Type help for commands";
  }
  private String drawBoardWhite(String[] params){
    return String.format("""
              1 2 3 4 5 6 7 8
            h %s 0 0 0 0 0 0 0 h
            g 0 0 0 0 0 0 0 0 g
            f 0 0 0 0 0 0 0 0 f
            e 0 0 0 0 0 0 0 0 e
            d 0 0 0 0 0 0 0 0 d
            c 0 0 0 0 0 0 0 0 c
            b X X X X X X X X b
            a Y Y Y Y Y Y Y Y a
              1 2 3 4 5 6 7 8
            """,
            WHITE_PAWN)
            ;
  }
  @Override
  public String help() {
    return """
            Commands:
            - getBoard games
            - surrender <gameName>
            - leave <GameID> <WHITE|BLACK>
            - quit
            - help""";
  }
}
