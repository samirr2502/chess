package client;

import org.junit.jupiter.api.*;

import ui.clients.ChessClient;
import ui.clients.LoggedInClient;
import ui.clients.LoggedOutClient;
import ui.Repl;
import ui.ServerFacade;

import static ui.Repl.PORT;

public class LoggedInClientTest {

  ServerFacade server;
  Repl repl;
  ChessClient currentClient;
  @BeforeEach
  public void setUp(){
    var serverUrl = "http://localhost:" + PORT;
    repl = new Repl(serverUrl);
    server = new ServerFacade(serverUrl);
    currentClient = new LoggedInClient(serverUrl);
    currentClient.eval("clear");

    LoggedOutClient loggedOutClient = new LoggedOutClient(serverUrl);
    loggedOutClient.eval("clear");

    loggedOutClient.eval("register samir 123 sam@123");
  }

  @Test
  public void joinBad(){
    String result = currentClient.eval("join 1");
    Assertions.assertEquals("Expected: <ListNumber> <WHITE|BLACK>", result);
  }

  @Test
  public void joinGoodInput(){
    currentClient.eval("create game1");

    String result = currentClient.eval("join 1 white");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Joined game: game1", result);
  }

  @Test
  public void createBadInput(){
    String result = currentClient.eval("create");
    Assertions.assertEquals("Expected: <Game Name>", result);
  }
  @Test
  public void createGoodInput(){
    String result = currentClient.eval("create game1");
    Assertions.assertEquals("Game game1, successfully created", result);
  }
  @Test
  public void observeBadInput(){
    currentClient.eval("create game1");

    String result = currentClient.eval("observe 1 white");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Expected <gameID>", result);
  }
  @Test
  public void observeGoodInput(){
    currentClient.eval("create game1");

    String result = currentClient.eval("observe 1");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Observing game: game1", result);
  }

  @Test
  public void listBadInput(){
    currentClient.eval("create game1");

    String result = currentClient.eval("list 1");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Expected: list", result);
  }

  @Test
  public void listGoodInput(){
    currentClient.eval("create game1");

    String result = currentClient.eval("list");
    Assertions.assertNotNull(result);
    String expected = """
                    1.) GameName: game1,
                    White Player: null,
                    Black Player: null
                   """;
    Assertions.assertEquals(expected, result);
  }
  @Test
  public void logoutBadInput(){
    currentClient.eval("create game1");

    String result = currentClient.eval("logout 1");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Expected: logout", result);
  }
  @Test
  public void logoutGoodInput(){

    String result = currentClient.eval("logout");
    Assertions.assertNotNull(result);
    String expected = "You are signed out \n\nType -help- for commands";
    Assertions.assertEquals(expected, result);
  }
  @Test
  public void help(){
    String result = currentClient.eval("help");
    String expected = """
            Commands:
               - list games
               - create <gameName>
               - join <GameID> <WHITE|BLACK>
               - observe <GameID>
               - logout
               - quit
               - help""";
    Assertions.assertEquals(expected, result);

  }

}
