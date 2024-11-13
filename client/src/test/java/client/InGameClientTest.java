package client;

import org.junit.jupiter.api.*;

import ui.Clients.ChessClient;
import ui.Clients.InGameClient;
import ui.Clients.LoggedOutClient;
import ui.Repl;
import ui.ServerFacade;

public class InGameClientTest {

  public  ServerFacade server = new ServerFacade("http://localhost:8080");
  Repl repl = new Repl("http://localhost:8080");
  private ChessClient currentClient = new InGameClient("http://localhost:8080");
  @BeforeEach
  public void setUp() throws Exception {
    server = new ServerFacade("http://localhost:8080");
    currentClient = new InGameClient("http://localhost:8080");
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
