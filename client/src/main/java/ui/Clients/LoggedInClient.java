package ui.Clients;

import model.AuthData;
import model.UserData;
import server.requests.AuthRequest;
import service.results.LoginResult;
import service.results.LogoutResult;
import ui.Repl;
import ui.ServerFacade;
import ui.State;

import java.util.Arrays;

public class LoggedInClient implements ChessClient{
  private final ServerFacade server;
  public LoggedInClient(String serverUrl){
    this.server = new ServerFacade(serverUrl);
  }
  /**
   * @param input to evaluate
   * @return Response
   */
  @Override
  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "list" -> listGames(params);
        case "create" -> createGame(params);
        case "join" -> joinGame(params);
        case "observe" -> joinGame(params);
        case "logout" -> logout(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (Exception ex) {
      return ex.getMessage();
    }
  }
  public String listGames(String ... params){
    return null;
  }
  public String createGame(String ... params){
    return null;
  }
  public String joinGame(String ... params){
    return null;
  }
  public String logout(String ... params) throws Exception {
    if (params.length ==0 ) {

      AuthRequest logoutRequest = new AuthRequest(Repl.authData.authToken());
      server.logoutUser(logoutRequest);
      Repl.state=State.LOGGED_OUT;
      return "You are signed out";
    }
    throw new Exception("Expected: logout");
  }

  /**
   * @return Help instructions
   */
  @Override
  public String help() {
    return """
            STATE: Logged In
            help info
            Useful Commands:

            - list games
            - create <gameName>
            - join <GameID> <WHITE|BLACK>
            - observe <GameID>
            - logout
            - quit
            - help""";
  }
}
