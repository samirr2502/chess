package client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Clients.ChessClient;
import ui.Clients.LoggedInClient;
import ui.Clients.LoggedOutClient;

public class LoggedInClientTest {

  private final ChessClient currentClient = new LoggedOutClient("urlTest");
  @BeforeEach
  public void setUp(){

  }

  @Test
  public void login(){

    String result = currentClient.eval("login");
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
