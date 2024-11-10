package ui.Clients;

import ui.ServerFacade;
import ui.State;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LoggedOutClient implements ChessClient{
  public LoggedOutClient(String serverUrl){

  }
  @Override
  public String eval(String input, State state) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "register" -> register(params);
        case "login" -> login(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (Exception ex) {
      return ex.getMessage();
    }
  }
  public String register(String[] params){
    return null;
  }
  public String login(String[] params){
    return null;
  }
  @Override
  public String help() {
    return "help info";
  }
}
