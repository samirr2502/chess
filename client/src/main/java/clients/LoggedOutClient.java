package clients;

import model.AuthData;
import model.UserData;
import results.LoginResult;
import ui.Repl;
import ui.ServerFacade;
import ui.State;

import java.util.Arrays;

public class LoggedOutClient implements ChessClient{
  private final ServerFacade server;
  public LoggedOutClient(String serverUrl){
    server = new ServerFacade(serverUrl);

  }
  @Override
  public void onStart() throws Exception {

  }
  @Override
  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "register" -> register(params);
        case "login" -> login(params);
        case "clear" -> clear();
        case "quit" -> "quit";
        default -> help();
      };
    } catch (Exception ex) {
      return ex.getMessage();
    }
  }
  public String register(String... params) throws Exception {
    if (params.length == 3) {
      String username = params[0];
      String password = params[1];
      String email = params[2];
      UserData userData = new UserData(username,password,email);
      LoginResult loginResult = server.registerUser(userData);
      Repl.authData = new AuthData(loginResult.authToken(),loginResult.username());
      Repl.state=State.LOGGED_IN;
      return String.format("You signed in as %s.\n\nType help for commands", loginResult.username());
    }
    throw new Exception("Expected: <username> <password> <email>");
  }
  public String login(String[] params) throws Exception {
    if (params.length ==2 ) {
      String username = params[0];
      String password = params[1];
      UserData userData = new UserData(username,password,"");
      LoginResult loginResult = server.loginUser(userData);
      Repl.authData = new AuthData(loginResult.authToken(),loginResult.username());
      Repl.state=State.LOGGED_IN;
      return String.format("You signed in as %s.\n\nType help for commands", loginResult.username());
    }
    throw new Exception("Expected: <username> <password>");
  }
  public String clear()throws Exception{
    try{
      server.clear();
      return "Database cleared";
    }catch (Exception ex){
      throw new Exception("Error deleting database");
    }
  }
  @Override
  public String help() {
    return """
            Commands:
            - register <username> <password> <email>
            - login <username> <password>
            - clear
            - quit
            - help""";
  }
}
