package ui;

import ui.Clients.ChessClient;
import ui.Clients.LoggedOutClient;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
  private final ChessClient client;
  private State state;

  public Repl(String serverUrl) {
    client = new LoggedOutClient(serverUrl);
  }
  public void run() {
    System.out.println("\uD83D\uDC36 Welcome to the Game Chess. Sign in to start.");
    System.out.print(client.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();
      try {
        result = client.eval(line, state);
        System.out.print(SET_TEXT_COLOR_BLUE + result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }
  private void printPrompt() {
    System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
  }
}
