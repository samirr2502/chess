package service;


import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.SQLDataAccess;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.authdao.SQLAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.gamedao.SQLGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.SQLUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.results.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
  private static MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
  private static MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
  private static MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
  static DataAccess dataAccess = new SQLDataAccess(new SQLAuthDAO(),new SQLGameDAO(), new SQLUserDAO());

  private static final Service SERVICE = new Service(dataAccess);
  private static UserData newUser;
  private static UserData existingUser;
  private static final String VALID_AUTH_TOKEN = "1234";
  private static final String INVALID_AUTH_TOKEN = "0000";


  @BeforeEach
  public void setUp() throws SQLException, DataAccessException {
    SERVICE.clear();
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
  public void createAuthDataTest() throws SQLException, DataAccessException {
    AuthData authData = SERVICE.createAuthData(existingUser, VALID_AUTH_TOKEN);
    assertNotNull(authData);
    assertEquals(VALID_AUTH_TOKEN, authData.authToken());
  }

  @Test
  public void createAuthDataBadTest() throws SQLException, DataAccessException {
    AuthData authData = SERVICE.createAuthData(existingUser, VALID_AUTH_TOKEN);
    memoryAuthDAO.addAuthData(authData);
    AuthData authData2 = SERVICE.createAuthData(existingUser, VALID_AUTH_TOKEN);
    assertNotNull(authData);
    assertNull(authData2);
  }

  @Test
  public void getAuthDataTest() throws SQLException, DataAccessException {

    AuthRequest authRequest = new AuthRequest("123");
    memoryAuthDAO.addAuthData(new AuthData("123", "NewUser"));
    AuthData authData = SERVICE.getAuthData(authRequest);
    assertEquals("123", authData.authToken());
  }

  @Test
  public void getAuthDataBadTest() throws SQLException, DataAccessException {

    AuthRequest authRequest = new AuthRequest("123");
    AuthData authData = SERVICE.getAuthData(authRequest);
    assertNull(authData);
  }

  @Test
  public void registerUserTest() throws SQLException, DataAccessException {

    LoginResult goodResult = SERVICE.registerUser(newUser, "123");
    assertEquals(goodResult.getClass(), LoginResult.class);
    assertEquals(goodResult.username(), "NewUser");
    assertEquals(goodResult.authToken(), "123");
  }


  @Test
  public void registerUserTwiceTest() throws SQLException, DataAccessException {
    LoginResult goodResult = SERVICE.registerUser(newUser, "123");
    assertEquals(goodResult.getClass(), LoginResult.class);
    assertEquals(goodResult.username(), "NewUser");
    assertEquals(goodResult.authToken(), "123");

    LoginResult badResult = SERVICE.registerUser(newUser, "123");
    assertNull(badResult);

    LoginResult badResult2 = SERVICE.registerUser(existingUser, "1234");
    assertNull(badResult2);

  }

  @Test
  public void loginRegisteredUserTest() throws SQLException, DataAccessException {
    LoginResult goodResult = SERVICE.loginUser(existingUser, "1234");

    assertEquals(goodResult.username(), "ExistingUser");
    assertEquals(goodResult.authToken(), "1234");
  }

  @Test
  public void loginUseNotRegisteredTest() throws SQLException, DataAccessException {
    //shouldn't log in with someone that hasn't been registered
    LoginResult nullResult = SERVICE.loginUser(newUser, "123");
    assertNull(nullResult);
  }

  @Test
  public void logoutExistingUserTest() throws SQLException, DataAccessException {
    //Should be able to log out
    AuthRequest logoutRequest = new AuthRequest("1234");
    memoryAuthDAO.addAuthData(new AuthData("1234", "ExistingUser"));
    LogoutResult goodLogoutResult = SERVICE.logoutUser(logoutRequest);

    assertNotNull(goodLogoutResult);
  }

  @Test
  public void logoutNonExistingUserTest() throws SQLException, DataAccessException {
    //Should return null because the authToken is not valid
    AuthRequest logoutRequest = new AuthRequest("000");
    LogoutResult goodLogoutResult = SERVICE.logoutUser(logoutRequest);

    assertNull(goodLogoutResult);
  }

  @Test
  public void getGamesTest() throws SQLException, DataAccessException {
    AuthRequest getGamesRequest = new AuthRequest("1234");
    memoryAuthDAO.addAuthData(new AuthData("1234", "ExistingUser"));

    GetGamesResult getGamesResult = SERVICE.getGames(getGamesRequest);
    assertNotNull(getGamesResult);
  }

  @Test
  public void getGamesNotAuthorizedTest() throws SQLException, DataAccessException {
    AuthRequest getGamesRequest = new AuthRequest("000");

    GetGamesResult getGamesResult = SERVICE.getGames(getGamesRequest);
    assertNull(getGamesResult);
  }

  @Test
  public void createGamesTest() throws SQLException, DataAccessException {
    AuthRequest authRequest = new AuthRequest(VALID_AUTH_TOKEN);
    CreateGameRequest createGameRequest = new CreateGameRequest("1");
    memoryAuthDAO.addAuthData(new AuthData(VALID_AUTH_TOKEN, "ExistingUser"));

    CreateGameResult createGameResult = SERVICE.createGame(authRequest, createGameRequest);
    assertNotNull(createGameResult);
  }

  @Test
  public void createExistingGame() throws SQLException, DataAccessException {
    //Auth not valid to create game
    AuthRequest authRequest = new AuthRequest(INVALID_AUTH_TOKEN);
    CreateGameRequest createGameRequest = new CreateGameRequest("1");
    //memoryAuthDAO.addAuthData(new AuthData(validAuthToken,"ExistingUser"));

    CreateGameResult createGameResult = SERVICE.createGame(authRequest, createGameRequest);
    assertNull(createGameResult);
  }

  @Test
  public void joinExistingGame() throws SQLException, DataAccessException {
    AuthRequest authRequest = new AuthRequest(VALID_AUTH_TOKEN);
    JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);

    CreateGameRequest createGameRequest = new CreateGameRequest("1");

    SERVICE.createGame(authRequest, createGameRequest);
    JoinGameResult joinGameResult = SERVICE.joinGame(authRequest, joinGameRequest);
    assertNotNull(joinGameResult);
  }

  @Test
  public void joinNunExistingGame() throws SQLException, DataAccessException {
    AuthRequest authRequest = new AuthRequest(VALID_AUTH_TOKEN);
    JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 0);

    CreateGameRequest createGameRequest = new CreateGameRequest("FirsGame_ID_1");

    SERVICE.createGame(authRequest, createGameRequest);
    JoinGameResult joinGameResult = SERVICE.joinGame(authRequest, joinGameRequest);
    assertNull(joinGameResult.gameData());
  }

  @Test
  public void clear() throws SQLException, DataAccessException {
    ClearResult clearResult = SERVICE.clear();
    assertNotNull(clearResult);
  }
}