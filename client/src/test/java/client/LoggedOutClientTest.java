package client;

import org.junit.jupiter.api.*;

import ui.Clients.ChessClient;
import ui.Clients.LoggedOutClient;
import ui.Repl;
import ui.ServerFacade;

public class LoggedOutClientTest {

  public  ServerFacade server = new ServerFacade("http://localhost:8080");
  Repl repl = new Repl("http://localhost:8080");
  private ChessClient currentClient = new LoggedOutClient("http://localhost:8080");
  @BeforeEach
  public void setUp() throws Exception {
    server = new ServerFacade("http://localhost:8080");
    currentClient = new LoggedOutClient("http://localhost:8080");
    currentClient.eval("clear");

  }

  @Test
  public void login_bad_input(){
    String result = currentClient.eval("login");
    Assertions.assertEquals("Expected: <username> <password>", result);
  }

  @Test
  public void login_good_input(){
    currentClient.eval("register samir 123 sam@123");

    String result = currentClient.eval("login samir 123");
    Assertions.assertNotNull(result);
    Assertions.assertEquals(String.format("You signed in as %s.\n\nType help for commands","samir"), result);
  }

  @Test
  public void register_badd_input(){
    String result = currentClient.eval("register 1");
    Assertions.assertEquals("Expected: <username> <password> <email>", result);
  }
  @Test
  public void register_already_taken_input(){
    String result = currentClient.eval("register samir 123 sam@123");
    Assertions.assertEquals("Already taken", result);
  }
  @Test
  public void register_good_input(){
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
