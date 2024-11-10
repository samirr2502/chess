package ui.Clients;

import ui.State;

public class LoggedInClient implements ChessClient{
  /**
   * @param prompt to evaluate
   * @return Response
   */
  @Override
  public String eval(String prompt, State state) {
    return "Quit Successful!";
  }

  /**
   * @return Help instructions
   */
  @Override
  public String help() {
    return null;
  }
}
