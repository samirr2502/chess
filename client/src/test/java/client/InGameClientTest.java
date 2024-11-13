package client;

import org.junit.jupiter.api.*;

import ui.Clients.ChessClient;
import ui.Clients.InGameClient;
import ui.Repl;
import ui.ServerFacade;

import static ui.Repl.PORT;

public class InGameClientTest {

  public  ServerFacade server;
  Repl repl;
  private ChessClient currentClient;
  @BeforeEach
  public void setUp() {
    var serverUrl = "http://localhost:" + PORT;
    repl = new Repl(serverUrl);
    server = new ServerFacade(serverUrl);
    currentClient = new InGameClient(serverUrl);
    currentClient.eval("clear");

  }

  @Test
  public void leave_good_input(){
    String result = currentClient.eval("leave");
    Assertions.assertEquals("You left the Game \n\n Type help for commands", result);
  }

  @Test
  public void leave_bad_input(){
    String result = currentClient.eval("leave 1");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Expected: leave", result);
  }

  @Test
  public void board_bad_input(){
    String result = currentClient.eval("board 1");
    Assertions.assertEquals("Expected: board", result);
  }
  @Test
  public void board_good_input(){
    String result = currentClient.eval("board");
    String expected= "";
    Assertions.assertEquals(expected, result);
  }
  @Test
  public void help(){
    String result = currentClient.eval("help");
    String expected = """
            Commands:
               - board - show current board
               - leave - leave game
               - quit
               - help""";
    Assertions.assertEquals(expected, result);

  }

}
