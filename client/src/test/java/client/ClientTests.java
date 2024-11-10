package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.Clients.ChessClient;
import ui.Clients.LoggedInClient;
import ui.Clients.LoggedOutClient;
import ui.State;

public class ClientTests {
  private final ChessClient currentClient = new LoggedOutClient("urlTest");
  @BeforeEach
  public void setUp(){

  }

  @Test
  public void evalLoggedOutClientTest(){
    ChessClient currentClient = new LoggedOutClient("urlTest");

    String result = currentClient.eval("quit", State.LOGGEDOUT);
    Assertions.assertEquals("Quit Successful!", result);
  }

  @Test
  public void evalLoggedInClientTest(){
    ChessClient currentClient = new LoggedInClient();
    String result = currentClient.eval("quit", State.LOGGEDIN);
    Assertions.assertEquals("Quit Successful!", result);
  }

  @AfterAll
  public static void clear(){

  }

}
