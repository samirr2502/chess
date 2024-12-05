package client;

import org.junit.jupiter.api.*;

import server.Server;
import clients.ChessClient;
import clients.InGameClient;
import ui.Repl;
import ui.ServerFacade;
import clients.LoggedOutClient;


public class InGameClientTest {
  public static Server server;
  static ServerFacade serverFacade;
  static String serverUrl;
  static int port;
  Repl repl;
  static ChessClient currentClient;
  @BeforeAll
  public static void init(){
    server = new Server();
    port =server.run(0);
  }

  @BeforeEach
  public void setUp() throws Exception {
    serverUrl = "http://localhost:" + port;
    repl = new Repl(serverUrl);
    serverFacade = new ServerFacade(serverUrl);
    currentClient = new InGameClient(serverUrl);
    currentClient.eval("clear");

  }
  @Test
  public void leaveGoodInput(){
    String result = currentClient.eval("leave");
    Assertions.assertEquals("You left the Game \n\n Type help for commands", result);
  }

  @Test
  public void leaveBadInput(){
    String result = currentClient.eval("leave 1");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Expected: leave", result);
  }

  @Test
  public void boarBadInput(){
    String result = currentClient.eval("board 1");
    Assertions.assertEquals("Expected: board", result);
  }
  @Test
  public void boardGoodInput(){
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

  @AfterAll
  public static void stop(){
    LoggedOutClient loggedOutClient = new LoggedOutClient(serverUrl);
    loggedOutClient.eval("clear");
    server.stop();
  }
}
