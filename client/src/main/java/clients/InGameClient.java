package clients;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import websocket.WebSocketFacade;
import ui.DrawnBoard;
import ui.Repl;
import ui.ServerFacade;
import ui.State;

import java.lang.reflect.Type;
import java.util.*;

import static java.lang.Integer.parseInt;

public class InGameClient implements ChessClient{
  private final ServerFacade server;
  private WebSocketFacade ws;
  private String serverUrl;
  //private Collection<ChessMove> validMoves = new ArrayList<>();
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
    ws.connectToGame(Repl.authData.username(),Repl.currentGame.gameID);
  }

  @Override
  public String eval(String input) {
      try {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
          case "board" -> getBoard(params);
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
      Repl.chessBoard = Repl.currentGameData.game().getBoard();
      return drawBoard();
    }
    throw new Exception("Expected: board");
  }
  private String getMoves(String[] params) throws Exception{
    if(params.length==1){
      ChessPosition position = parsePosition(params[0]);
      var validMoves= Repl.currentGameData.game().validMoves(position);
      highLightValidMoves(validMoves);
    }
    return "";
  }

  private void highLightValidMoves(Collection<ChessMove> validMoves) {
    DrawnBoard.run(Repl.lastJoinedGameColor,Repl.chessBoard,validMoves);
    validMoves.clear();
  }

  private String makeMove(String[] params) {
    if(params.length ==2) {
      if (Repl.currentGameData.game().getTeamTurn() == Repl.lastJoinedGameColor) {
        ChessPosition start = parsePosition(params[0]);
        ChessPosition end = parsePosition(params[1]);
        var validMoves= Repl.currentGameData.game().validMoves(start);
        if(validMoves!= null){
          for (ChessMove move:validMoves){
            if(move.getEndPosition() == end){
              if (move.getPromotionPiece() != null){
                //promptPromptionPiece(move);
              }
            }
          }
        }

        //ws.makeMove();
        return "";

      }
    }
    return "";
  }
private ChessPosition parsePosition(String pos){
    int row= 0;
    int col =0;

    if(pos.length()==2 && pos.toCharArray()[0] == 'a' ) {
      char[] posCharArray = pos.toCharArray();
      if (lettersToNumbers.get(posCharArray[0]) != null
              && (posCharArray[1] > 0 && posCharArray[1] <= 8)) {
        col = posCharArray[0];
        row = posCharArray[1];
        return new ChessPosition(row - 1, col - 1);
      }
    }
    return null;
}


  private String leaveGame(String[] params) throws Exception {
    if(params.length==0) {
      switch (Repl.lastJoinedGameColor) {
        case WHITE ->Repl.currentGame.whiteUsername = null;
        case BLACK -> Repl.currentGame.blackUsername =null;
      }
      ws.leaveGame(Repl.authData.authToken(), Repl.currentGame.gameID);
      Repl.state = State.LOGGED_IN;
      return "You left the Game \n\n Type help for commands";
    }
    throw new Exception("Expected: leave");
  }
  private String resignGame(String[] params) {
    return "";
  }
  private String drawBoard(){

     DrawnBoard.run(ChessGame.TeamColor.WHITE,Repl.chessBoard,validMoves);
     System.out.println();
     DrawnBoard.run(ChessGame.TeamColor.BLACK,Repl.chessBoard, validMoves);
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

  /**
   *
   */

}
