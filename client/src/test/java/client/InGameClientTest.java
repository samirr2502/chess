package client;

import org.junit.jupiter.api.*;

import server.Server;
import ui.clients.ChessClient;
import ui.clients.InGameClient;
import ui.Repl;
import ui.ServerFacade;
import ui.clients.LoggedOutClient;

import static ui.Repl.PORT;

public class InGameClientTest {

  public static Server server;
  static ServerFacade serverFacade;
  static int port;
  Repl repl;
  static ChessClient currentClient;
  @BeforeAll
  public static void init(){
    server = new Server();
    port =server.run(0);
  }
  @AfterAll
  public static void stop(){
    currentClient.eval("clear");
    server.stop();
  }
  @BeforeEach
  public void setUp(){
    var serverUrl = "http://localhost:" + port;
    repl = new Repl(serverUrl);
    serverFacade = new ServerFacade(serverUrl);
    currentClient = new InGameClient(serverUrl);
    currentClient.eval("clear");

    LoggedOutClient loggedOutClient = new LoggedOutClient(serverUrl);
    loggedOutClient.eval("clear");
    loggedOutClient.eval("register samir 123 sam@123");
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

}
