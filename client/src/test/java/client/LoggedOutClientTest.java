package client;

import org.junit.jupiter.api.*;

import ui.clients.ChessClient;
import ui.clients.LoggedOutClient;
import ui.Repl;
import ui.ServerFacade;

import static ui.Repl.PORT;

public class LoggedOutClientTest {

  public  ServerFacade server;
  Repl repl;
  private ChessClient currentClient;
  @BeforeEach
  public void setUp()  {
    var serverUrl = "http://localhost:" + PORT;
    repl = new Repl(serverUrl);
    server = new ServerFacade(serverUrl);
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
