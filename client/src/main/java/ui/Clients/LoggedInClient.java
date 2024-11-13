package ui.Clients;

import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.results.*;
import ui.Repl;
import ui.ServerFacade;
import ui.State;

import java.util.Arrays;

import static java.lang.Integer.parseInt;

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
        case "join", "observe" -> joinGame(params);
        case "logout" -> logout(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (Exception ex) {
      return ex.getMessage();
    }
  }
  public String listGames(String ... params) throws Exception {
    if (params.length ==0 ) {

      AuthRequest authRequest = new AuthRequest(Repl.authData.authToken());
      GetGamesResult getGamesResult = server.getGames(authRequest);
      Repl.games = getGamesResult.games;
      var printList ="";
      int index=1;
      for (GameResult game :Repl.games){
        printList =printList.concat(String.format("""
                   %d.) GameName: %s,
                   White Player: %s,
                   Black Player: %s
                  """,index,game.gameName,game.whiteUsername, game.blackUsername));
       index++;
      }
      return printList;
    }
    throw new Exception("Expected: logout");
  }
  public String createGame(String ... params) throws Exception {
    if (params.length ==1 ) {
      AuthRequest authRequest = new AuthRequest(Repl.authData.authToken());
      CreateGameRequest createGameRequest = new CreateGameRequest(params[0]);
      CreateGameResult createGameResult = server.createGame(authRequest, createGameRequest);

      return createGameResult.toString();
    }
    throw new Exception("Expected: <GameName>");
  }
  public String joinGame(String ... params) throws Exception {

    if (params.length ==1) {
      Repl.state = State.IN_GAME;
      Repl.currentGame= Repl.games.get(parseInt(params[0])-1);
      return "Observing game: " + Repl.currentGame.gameName;
    }
    if (params.length ==2 ) {
      AuthRequest authRequest = new AuthRequest(Repl.authData.authToken());
      JoinGameRequest joinGameRequest = new JoinGameRequest(params[1].toUpperCase(),Repl.games.get((parseInt(params[0])-1)).gameID);
      JoinGameResult joinGameResult = server.joinGame(authRequest, joinGameRequest);
      Repl.currentGame= Repl.games.get((parseInt(params[0])-1));
      Repl.state = State.IN_GAME;
      return "Joined game: " + Repl.currentGame.gameName;
    }
    throw new Exception("Expected: <GameName>");  }
  public String logout(String ... params) throws Exception {
    if (params.length ==0 ) {
      AuthRequest logoutRequest = new AuthRequest(Repl.authData.authToken());
      server.logoutUser(logoutRequest);
      Repl.state=State.LOGGED_OUT;
      return "You are signed out \n\nType -help- for commands";
    }
    throw new Exception("Expected: logout");
  }

  /**
   * @return Help instructions
   */
  @Override
  public String help() {
    return """
            Commands:
            - list games
            - create <gameName>
            - join <GameID> <WHITE|BLACK>
            - observe <GameID>
            - logout
            - quit
            - help""";
  }
}
