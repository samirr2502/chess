package ui.Clients;

import ui.ServerFacade;
import ui.State;

public class InGameClient implements ChessClient{
  private final ServerFacade server;
  public InGameClient(String serverUrl){
    server = new ServerFacade(serverUrl);

  }
  @Override
  public String eval(String prompt) {
    return null;
  }

  @Override
  public String help() {
    return null;
  }
}
