package client;

import org.junit.jupiter.api.*;
import ui.clients.ChessClient;
import ui.clients.LoggedInClient;
import ui.clients.LoggedOutClient;

public class ClientTests {
  @BeforeEach
  public void setUp(){

  }

  @Test
  public void evalLoggedOutClientTest(){
    ChessClient currentClient = new LoggedOutClient("urlTest");

    String result = currentClient.eval("quit");
    Assertions.assertEquals("quit", result);
  }

  @Test
  public void evalLoggedInClientTest(){
    ChessClient currentClient = new LoggedInClient("urlTest");
    String result = currentClient.eval("quit");
    Assertions.assertEquals("quit", result);
  }

  @AfterAll
  public static void clear(){

  }

}
