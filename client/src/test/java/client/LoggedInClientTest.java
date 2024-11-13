package client;

import org.junit.jupiter.api.*;

import ui.Clients.ChessClient;
import ui.Clients.LoggedInClient;
import ui.Clients.LoggedOutClient;
import ui.Repl;
import ui.ServerFacade;

public class LoggedInClientTest {

  public  ServerFacade server = new ServerFacade("http://localhost:8080");
  Repl repl = new Repl("http://localhost:8080");
  private ChessClient currentClient = new LoggedInClient("http://localhost:8080");
  @BeforeEach
  public void setUp() throws Exception {
    server = new ServerFacade("http://localhost:8080");
    currentClient = new LoggedInClient("http://localhost:8080");

    LoggedOutClient loggedOutClient = new LoggedOutClient("http://localhost:8080");
    loggedOutClient.eval("clear");

    loggedOutClient.eval("register samir 123 sam@123");
  }

  @Test
  public void join_bad(){
    String result = currentClient.eval("join 1");
    Assertions.assertEquals("Expected: <ListNumber> <WHITE|BLACK>", result);
  }

  @Test
  public void join_good_input(){
    currentClient.eval("create game1");

    String result = currentClient.eval("join 1 white");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Joined game: game1", result);
  }

  @Test
  public void create_bad_input(){
    String result = currentClient.eval("create");
    Assertions.assertEquals("Expected: <Game Name>", result);
  }
  @Test
  public void creat_good_input(){
    String result = currentClient.eval("create game1");
    Assertions.assertEquals("Game game1, successfully created", result);
  }
  @Test
  public void observe_bad_input(){
    currentClient.eval("create game1");

    String result = currentClient.eval("observe 1 white");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Expected <gameID>", result);
  }
  @Test
  public void observe_good_input(){
    currentClient.eval("create game1");

    String result = currentClient.eval("observe 1");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Observing game: game1", result);
  }

  @Test
  public void list_bad_input(){
    currentClient.eval("create game1");

    String result = currentClient.eval("list 1");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Expected: list", result);
  }

  @Test
  public void list_good_input(){
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
  public void logout_bad_input(){
    currentClient.eval("create game1");

    String result = currentClient.eval("logout 1");
    Assertions.assertNotNull(result);
    Assertions.assertEquals("Expected: logout", result);
  }
  @Test
  public void logout_good_input(){

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
