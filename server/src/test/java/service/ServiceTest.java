package service;


import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.results.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
  private static MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
  private static MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
  private static MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
  private static final Service SERVICE = new Service();
  private static UserData newUser;
  private static UserData existingUser;
  private static final String VALID_AUTH_TOKEN = "1234";
  private static final String INVALID_AUTH_TOKEN = "0000";


  @BeforeEach
  public void setUp() {
    SERVICE.clear(memoryAuthDAO, memoryUserDAO, memoryGameDAO);
    //New User information with no valid log in (needs to register)
    newUser = new UserData("NewUser", "pass2", "user1@email.com");
    memoryUserDAO = new MemoryUserDAO();
    memoryAuthDAO = new MemoryAuthDAO();
    memoryGameDAO = new MemoryGameDAO();

    //Add Existing user with valid login
    existingUser = new UserData("ExistingUser", "pass1", "user12@email.com");
    memoryUserDAO.addUser(existingUser);
    memoryAuthDAO.addAuthData(new AuthData("ExistingUser", VALID_AUTH_TOKEN));
  }

  @Test
  public void createAuthDataTest() {
    AuthData authData = SERVICE.createAuthData(existingUser, VALID_AUTH_TOKEN, memoryAuthDAO);
    assertNotNull(authData);
    assertEquals(VALID_AUTH_TOKEN, authData.authToken());
  }

  @Test
  public void createAuthDataBadTest() {
    AuthData authData = SERVICE.createAuthData(existingUser, VALID_AUTH_TOKEN, memoryAuthDAO);
    memoryAuthDAO.addAuthData(authData);
    AuthData authData2 = SERVICE.createAuthData(existingUser, VALID_AUTH_TOKEN, memoryAuthDAO);
    assertNotNull(authData);
    assertNull(authData2);
  }

  @Test
  public void getAuthDataTest() {

    AuthRequest authRequest = new AuthRequest("123");
    memoryAuthDAO.addAuthData(new AuthData("123", "NewUser"));
    AuthData authData = SERVICE.getAuthData(authRequest, memoryAuthDAO);
    assertEquals("123", authData.authToken());
  }

  @Test
  public void getAuthDataBadTest() {

    AuthRequest authRequest = new AuthRequest("123");
    AuthData authData = SERVICE.getAuthData(authRequest, memoryAuthDAO);
    assertNull(authData);
  }

  @Test
  public void registerUserTest() {

    LoginResult goodResult = SERVICE.registerUser(newUser, "123", memoryUserDAO, memoryAuthDAO);
    assertEquals(goodResult.getClass(), LoginResult.class);
    assertEquals(goodResult.username(), "NewUser");
    assertEquals(goodResult.authToken(), "123");
  }


  @Test
  public void registerUserTwiceTest() {
    LoginResult goodResult = SERVICE.registerUser(newUser, "123", memoryUserDAO, memoryAuthDAO);
    assertEquals(goodResult.getClass(), LoginResult.class);
    assertEquals(goodResult.username(), "NewUser");
    assertEquals(goodResult.authToken(), "123");

    LoginResult badResult = SERVICE.registerUser(newUser, "123", memoryUserDAO, memoryAuthDAO);
    assertNull(badResult);

    LoginResult badResult2 = SERVICE.registerUser(existingUser, "1234", memoryUserDAO, memoryAuthDAO);
    assertNull(badResult2);

  }

  @Test
  public void loginRegisteredUserTest() {
    LoginResult goodResult = SERVICE.loginUser(existingUser, "1234", memoryUserDAO, memoryAuthDAO);

    assertEquals(goodResult.username(), "ExistingUser");
    assertEquals(goodResult.authToken(), "1234");
  }

  @Test
  public void loginUseNotRegisteredTest() {
    //shouldn't log in with someone that hasn't been registered
    LoginResult nullResult = SERVICE.loginUser(newUser, "123", memoryUserDAO, memoryAuthDAO);
    assertNull(nullResult);
  }

  @Test
  public void logoutExistingUserTest() {
    //Should be able to log out
    AuthRequest logoutRequest = new AuthRequest("1234");
    memoryAuthDAO.addAuthData(new AuthData("1234", "ExistingUser"));
    LogoutResult goodLogoutResult = SERVICE.logoutUser(logoutRequest, memoryAuthDAO);

    assertNotNull(goodLogoutResult);
  }

  @Test
  public void logoutNonExistingUserTest() {
    //Should return null because the authToken is not valid
    AuthRequest logoutRequest = new AuthRequest("000");
    LogoutResult goodLogoutResult = SERVICE.logoutUser(logoutRequest, memoryAuthDAO);

    assertNull(goodLogoutResult);
  }

  @Test
  public void getGamesTest() {
    AuthRequest getGamesRequest = new AuthRequest("1234");
    memoryAuthDAO.addAuthData(new AuthData("1234", "ExistingUser"));

    GetGamesResult getGamesResult = SERVICE.getGames(getGamesRequest, memoryGameDAO, memoryAuthDAO);
    assertNotNull(getGamesResult);
  }

  @Test
  public void getGamesNotAuthorizedTest() {
    AuthRequest getGamesRequest = new AuthRequest("000");

    GetGamesResult getGamesResult = SERVICE.getGames(getGamesRequest, memoryGameDAO, memoryAuthDAO);
    assertNull(getGamesResult);
  }

  @Test
  public void createGamesTest() {
    AuthRequest authRequest = new AuthRequest(VALID_AUTH_TOKEN);
    CreateGameRequest createGameRequest = new CreateGameRequest("1");
    memoryAuthDAO.addAuthData(new AuthData(VALID_AUTH_TOKEN, "ExistingUser"));

    CreateGameResult createGameResult = SERVICE.createGame(authRequest, createGameRequest, memoryGameDAO, memoryAuthDAO);
    assertNotNull(createGameResult);
  }

  @Test
  public void createExistingGame() {
    //Auth not valid to create game
    AuthRequest authRequest = new AuthRequest(INVALID_AUTH_TOKEN);
    CreateGameRequest createGameRequest = new CreateGameRequest("1");
    //memoryAuthDAO.addAuthData(new AuthData(validAuthToken,"ExistingUser"));

    CreateGameResult createGameResult = SERVICE.createGame(authRequest, createGameRequest, memoryGameDAO, memoryAuthDAO);
    assertNull(createGameResult);
  }

  @Test
  public void joinExistingGame() {
    AuthRequest authRequest = new AuthRequest(VALID_AUTH_TOKEN);
    JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);

    CreateGameRequest createGameRequest = new CreateGameRequest("1");

    SERVICE.createGame(authRequest, createGameRequest, memoryGameDAO, memoryAuthDAO);
    JoinGameResult joinGameResult = SERVICE.joinGame(authRequest, joinGameRequest, memoryAuthDAO, memoryGameDAO);
    assertNotNull(joinGameResult);
  }

  @Test
  public void joinNunExistingGame() {
    AuthRequest authRequest = new AuthRequest(VALID_AUTH_TOKEN);
    JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 0);

    CreateGameRequest createGameRequest = new CreateGameRequest("FirsGame_ID_1");

    SERVICE.createGame(authRequest, createGameRequest, memoryGameDAO, memoryAuthDAO);
    JoinGameResult joinGameResult = SERVICE.joinGame(authRequest, joinGameRequest, memoryAuthDAO, memoryGameDAO);
    assertNull(joinGameResult.gameData());
  }

  @Test
  public void clear() {
    ClearResult clearResult = SERVICE.clear(memoryAuthDAO, memoryUserDAO, memoryGameDAO);
    assertNotNull(clearResult);
  }
}