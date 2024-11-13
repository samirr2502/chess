package client;

import org.junit.jupiter.api.*;
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

    String result = currentClient.eval("quit");
    Assertions.assertEquals("Quit Successful!", result);
  }

  @Test
  public void evalLoggedInClientTest(){
    ChessClient currentClient = new LoggedInClient("urlTest");
    String result = currentClient.eval("quit");
    Assertions.assertEquals("Quit Successful!", result);
  }

  @AfterAll
  public static void clear(){

  }

}
