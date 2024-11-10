package ui.Clients;

import ui.State;

public interface ChessClient {
  String eval(String prompt, State state);
  String help();
  }
