package clients;

import chess.ChessGame;
import requests.AuthRequest;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import results.*;
import ui.Repl;
import ui.ServerFacade;
import ui.State;

import java.util.Arrays;

import static java.lang.Integer.parseInt;
import static ui.Repl.*;

public class LoggedInClient implements ChessClient{
  private final ServerFacade server;
  public LoggedInClient(String serverUrl){
    this.server = new ServerFacade(serverUrl);
  }
  @Override
  public void onStart() throws Exception {
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
        case "observe" -> observeGame(params);
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
    throw new Exception("Expected: list");
  }
  public String createGame(String ... params) throws Exception {
    if (params.length ==1 ) {
      AuthRequest authRequest = new AuthRequest(Repl.authData.authToken());
      CreateGameRequest createGameRequest = new CreateGameRequest(params[0]);
      server.createGame(authRequest, createGameRequest);
      //Add to list
      GetGamesResult getGamesResult = server.getGames(authRequest);
      Repl.games = getGamesResult.games;
      return String.format("Game %s, successfully created", createGameRequest.gameName());
    } else{
      return "Expected: <GameName>";
    }
  }
  public String observeGame(String... params) throws Exception{
    if (params.length == 1) {
      createGameListIfNotExist();
      checkIDNotString(params);
      if (Repl.games.size() >= parseInt(params[0]) && parseInt(params[0])>0) {
        Repl.currentGame = Repl.games.get(parseInt(params[0]) - 1);
        Repl.state = State.IN_GAME;
        inGameClient.onStart();
        //inGameClient.eval("board");
        return String.format("Observing game: %s\n\nUse -help to see options", Repl.currentGame.gameName);
      } else{
        return "Game not listed. \n\nuse -list to see games";
      }
    }
    throw new Exception("Expected <gameID>");
  }
  public String joinGame(String ... params) throws Exception {
    if (params.length == 2 &&
            (params[1].equalsIgnoreCase("WHITE") || params[1].equalsIgnoreCase("BLACK"))) {
      createGameListIfNotExist();
      checkIDNotString(params);
      if(Repl.games.size()>= parseInt(params[0]) && parseInt(params[0])>0) {
        AuthRequest authRequest = new AuthRequest(Repl.authData.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest(params[1].toUpperCase(), Repl.games.get((parseInt(params[0]) - 1)).gameID);
        server.joinGame(authRequest, joinGameRequest);

        Repl.currentGame = Repl.games.get((parseInt(params[0]) - 1));
        switch (params[1].toUpperCase()){
          case "WHITE" -> lastJoinedGameColor = ChessGame.TeamColor.WHITE;
          case "BLACK" -> lastJoinedGameColor = ChessGame.TeamColor.BLACK;
        }
        Repl.state = State.IN_GAME;
        inGameClient.onStart();
      return String.format("Joined  game: %s\n\nUse -help to see options", Repl.currentGame.gameName);
      }else{
      return "Game not listed. \n\nuse -list to see games";
    }
    }
    throw new Exception("Expected: <ListNumber> <WHITE|BLACK>");
  }

  private static void checkIDNotString(String[] params) {
    try {
      parseInt(params[0]);
    }catch (NumberFormatException ex){
      throw new NumberFormatException("GameID needs to be a number");
    }
  }


  private void createGameListIfNotExist() throws Exception {
    if (Repl.games== null){
    AuthRequest authRequest = new AuthRequest(Repl.authData.authToken());
    GetGamesResult getGamesResult = server.getGames(authRequest);
    Repl.games = getGamesResult.games;
  }
  }

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

  /**
   * @throws Exception
   */

}
