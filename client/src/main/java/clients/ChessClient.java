package clients;

public interface ChessClient {
  String eval(String prompt);
  String help();
  void onStart() throws Exception;
  }

