package client;

import org.junit.jupiter.api.*;

import server.Server;
import ui.clients.ChessClient;
import ui.clients.LoggedOutClient;
import ui.Repl;
import ui.ServerFacade;

import static ui.Repl.PORT;

public class LoggedOutClientTest {
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
    currentClient = new LoggedOutClient(serverUrl);
    currentClient.eval("clear");


  }

  @Test
  public void loginBadInput(){
    String result = currentClient.eval("login");
    Assertions.assertEquals("Expected: <username> <password>", result);
  }

  @Test
  public void loginGoodInput(){
    currentClient.eval("register samir 123 sam@123");

    String result = currentClient.eval("login samir 123");
    Assertions.assertNotNull(result);
    Assertions.assertEquals(String.format("You signed in as %s.\n\nType help for commands","samir"), result);
  }

  @Test
  public void registerBadInput(){
    String result = currentClient.eval("register 1");
    Assertions.assertEquals("Expected: <username> <password> <email>", result);
  }
  @Test
  public void registerAlreadyTakenInput(){
    currentClient.eval("register samir 123 sam@123");

    String result = currentClient.eval("register samir 123 sam@123");
    Assertions.assertEquals("Already taken", result);
  }
  @Test
  public void registerGoodInput(){
    String result = currentClient.eval("register samir14 123 sam@123");
    Assertions.assertEquals(String.format("You signed in as %s.\n\nType help for commands","samir14"), result);

  }
  @Test
  public void help(){
    String result = currentClient.eval("help");
    String expected = """
            Commands:
            - register <username> <password> <email>
            - login <username> <password>
            - clear
            - quit
            - help""";
    Assertions.assertEquals(expected, result);

  }

}
