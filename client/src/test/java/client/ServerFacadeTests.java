package client;

import model.UserData;
import org.junit.jupiter.api.*;
import requests.AuthRequest;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import results.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;
import static ui.Repl.PORT;


public class ServerFacadeTests {

    private static ServerFacade serverFacade;
    private static Server server;
    public static int port;
    @BeforeAll
    public static void init() {
        server =new Server();
        port =server.run(PORT);

        System.out.println("Started test HTTP server on " +port);
    }
    @BeforeEach
    public void setUp(){
        var serverUrl = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverUrl);
    }


    @Test
    public void registerUserGood() throws Exception {
        LoginResult user = serverFacade.registerUser(new UserData("samirr14","123","samir123"));
        assertNotNull(user);
    }
    @Test
    public void registerUserBad() {
        try {
            serverFacade.registerUser(new UserData("samir", "123", "samir123"));
            serverFacade.registerUser(new UserData("samir", "123", "samir123"));
        }catch (Exception ex){
            String expected = "Already taken";
            assertEquals(expected, ex.getMessage());
        }
    }
    @Test
    public void loginUserGood() throws Exception {
        serverFacade.registerUser(new UserData("samirr14", "123", "samir123"));
        LoginResult userLogin = serverFacade.loginUser(new UserData("samirr14", "123", "samir123"));
        assertNotNull(userLogin);
    }
    @Test
    public void loinUserBad(){
        try {
            LoginResult loginUser = serverFacade.loginUser(new UserData("samirr14", "123", "samir123"));
            assertNull(loginUser);
        }catch (Exception ex){
            String expected = "Unauthorized";
            assertEquals(expected, ex.getMessage());
        }
    }
    @Test
    public void logoutUserGood() throws Exception {
        LoginResult user = serverFacade.registerUser(new UserData("samirr14", "123", "samir123"));
        LogoutResult userLogout = serverFacade.logoutUser(new AuthRequest(user.authToken()));
        assertNotNull(userLogout);
    }
    @Test
    public void logoutUserBad(){
        try {
            LogoutResult logoutUser = serverFacade.logoutUser(new AuthRequest("WrongAUthData"));
            assertNull(logoutUser);
        }catch (Exception ex){
            String expected = "Unauthorized";
            assertEquals(expected, ex.getMessage());
        }
    }
    @Test
    public void createGameGood() throws Exception {
        LoginResult user = serverFacade.registerUser(new UserData("samirr14", "123", "samir123"));
        CreateGameResult game = serverFacade.createGame(new AuthRequest(user.authToken()), new CreateGameRequest("game1"));
        assertNotNull(game);
    }
    @Test
    public void createGameBad(){
        try {
            CreateGameResult game = serverFacade.createGame(new AuthRequest("notExistingAuthData"), new CreateGameRequest("game1"));
            assertNotNull(game);
        }catch (Exception ex){
            String expected = "Unauthorized";
            assertEquals(expected, ex.getMessage());
        }
    }
    @Test
    public void joinGameGood() throws Exception {
        LoginResult user = serverFacade.registerUser(new UserData("samirr14", "123", "samir123"));
        serverFacade.createGame(new AuthRequest(user.authToken()), new CreateGameRequest("game1"));
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);
        JoinGameResult joinGameResult = serverFacade.joinGame(new AuthRequest(user.authToken()),joinGameRequest);
        assertNotNull(joinGameResult);
    }
    @Test
    public void joinGameBad(){
        try {
            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);
            JoinGameResult game = serverFacade.joinGame(new AuthRequest("notExistingAuthData"),joinGameRequest);
            assertNull(game);
        } catch (Exception ex){
            String expected = "Bad request";
            assertEquals(expected, ex.getMessage());
        }
    }
    @Test
    public void listGamesGood() throws Exception {
        LoginResult user = serverFacade.registerUser(new UserData("samirr14", "123", "samir123"));
        serverFacade.createGame(new AuthRequest(user.authToken()), new CreateGameRequest("game1"));
        GetGamesResult getGamesResult = serverFacade.getGames(new AuthRequest(user.authToken()));
        assertNotNull(getGamesResult);
    }
    @Test
    public void listGamesBaf(){
        try {
            LoginResult user = serverFacade.registerUser(new UserData("samirr14", "123", "samir123"));
            serverFacade.createGame(new AuthRequest(user.authToken()), new CreateGameRequest("game1"));
            GetGamesResult getGamesResult = serverFacade.getGames(new AuthRequest("notAnAuthToken"));
            assertNull(getGamesResult);
        } catch (Exception ex){
            String expected = "Unauthorized";
            assertEquals(expected, ex.getMessage());
        }
    }
    @AfterEach
    public void clear() throws Exception {
        serverFacade.clear();

    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
