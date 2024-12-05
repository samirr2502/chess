package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import results.GameResult;
import clients.ChessClient;
import clients.InGameClient;
import clients.LoggedInClient;
import clients.LoggedOutClient;

import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl {
  private ChessClient currentClient;
  private final ChessClient loggedOutClient;
  private final ChessClient loggedInClient;
  public static ChessClient inGameClient;
  public static AuthData authData;
  public static State state;
  public static ArrayList<GameResult> games;
  public static GameResult currentGame;
  public static GameData currentGameData;
  public static ChessGame.TeamColor lastJoinedGameColor;
  public static ChessBoard chessBoard = new ChessBoard();

  public Repl(String serverUrl) throws Exception {
    currentClient = new LoggedOutClient(serverUrl);
    loggedOutClient = new LoggedOutClient(serverUrl);
    loggedInClient = new LoggedInClient(serverUrl);
    inGameClient = new InGameClient(serverUrl);
  }
  public void run() {
    System.out.println("Welcome to the Game Chess. Sign in to start.");
    System.out.print(currentClient.help());
    state = State.LOGGED_OUT;
    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();
      try {
        result = currentClient.eval(line);
        System.out.print(SET_TEXT_COLOR_BLUE + result);
        switch (state){
          case State.LOGGED_IN -> currentClient =loggedInClient;
          case State.IN_GAME -> currentClient =inGameClient;
          default -> currentClient= loggedOutClient;
        }
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }
  private void printPrompt() {
    System.out.print("\n["+ SET_TEXT_COLOR_GREEN+ state+ "] " + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
  }
}
