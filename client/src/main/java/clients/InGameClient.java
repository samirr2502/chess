package clients;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import websocket.WebSocketFacade;
import ui.DrawnBoard;
import ui.Repl;
import ui.ServerFacade;
import ui.State;

import java.util.*;
import com.google.gson.Gson;


public class InGameClient implements ChessClient{
  private final ServerFacade server;
  private WebSocketFacade ws;
  private String serverUrl;
  private Collection<ChessMove> validMoves = new ArrayList<>();
  private Map<Character, Integer> lettersToNumbers = HashMap.newHashMap(8);

  public InGameClient(String serverUrl) throws Exception {
   this.server = new ServerFacade(serverUrl);
   this.serverUrl = serverUrl;
   lettersToNumbers.put('a',1);
    lettersToNumbers.put('b',2);
    lettersToNumbers.put('c',3);
    lettersToNumbers.put('d',4);
    lettersToNumbers.put('e',5);
    lettersToNumbers.put('f',6);
    lettersToNumbers.put('g',7);
    lettersToNumbers.put('h',8);
  }

  @Override
  public void onStart() throws Exception {
    ws = new WebSocketFacade(this.serverUrl);
    ws.connectToGame(Repl.authData.authToken(),Repl.currentGame.gameID);

  }

  @Override
  public String eval(String input) {
      try {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
          case "board" -> drawBoard();
          case "moves" -> getMoves(params);
          case "move" -> makeMove(params);
          case "leave" -> leaveGame(params);
          case "resign" -> resignGame(params);
          case "quit" -> "quit";
          default -> help();
        };
      } catch (Exception ex) {
        return ex.getMessage();
      }

  }
  private String getBoard(String[] params) throws Exception {
    if(params.length==0) {
      return drawBoard();
    }
    throw new Exception("Expected: board");
  }
  private String getMoves(String[] params) throws Exception{
    if(params.length==1){
      ChessPosition position = parsePosition(params[0]);
//      validMoves= Repl.currentGameData.game().validMoves(position);
      highLightValidMoves(validMoves);
    }
    return "";
  }

  private void highLightValidMoves(Collection<ChessMove> validMoves) {
    //DrawnBoard.run(Repl.lastJoinedGameColor,Repl.currentGameData.game().getBoard(), validMoves);
    validMoves.clear();
  }

  private String makeMove(String[] params) throws Exception {
    if(params.length ==2) {
        ChessPosition start = parsePosition(params[0]);
        ChessPosition end = parsePosition(params[1]);
        ChessMove move = new ChessMove(start,end,null);
        ws.makeMove(Repl.authData.authToken(), Repl.currentGameData.gameID(), move);
    }
    return "";
  }
private ChessPosition parsePosition(String pos){
    int row= 1;
    int col =1;
    char[] posCharArray = pos.toCharArray();
    if(pos.length()==2 && lettersToNumbers.get(posCharArray[0])!= null) {
      int c = lettersToNumbers.get(posCharArray[0]);
      int r = Character.getNumericValue(posCharArray[1]);
      if ((r > 0 && r <= 8)) {
        row = r;
        col =c;
        return new ChessPosition(row , col );
      }
    }
    return new ChessPosition(row,col);
}


  private String leaveGame(String[] params) throws Exception {
    if(params.length==0) {
      ws.leaveGame(Repl.authData.authToken(), Repl.currentGame.gameID);
      Repl.state = State.LOGGED_IN;
      return "You left the Game \n\n Type help for commands";
    }
    throw new Exception("Expected: leave");
  }
  private String resignGame(String[] params) throws Exception {
    Scanner scanner= new Scanner(System.in);
    if(Objects.equals(scanner.nextLine(), "yes")){
      ws.resign(Repl.authData.authToken(), Repl.currentGame.gameID);
    }
    return "";
  }

  private String drawBoard() throws Exception {

    ws.drawBoard();
    return "";

  }
  @Override
  public String help() {
    return """
            Commands:
               - board - redraw current board
               - move <Start Position> <End Position> - Move piece
               - moves <Piece Position> - highlight legal moves
               - leave - leave game
               - resign - give up
               - quit - exit program
               - help""";
  }

}
