package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SQLDataAccess;
import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.authdao.SQLAuthDAO;
import dataaccess.gamedao.GameDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.gamedao.SQLGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.SQLUserDAO;
import dataaccess.userdao.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.results.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
  private static final DataAccess DATA_ACCESS;

  static {
    //DATA_ACCESS = new MemoryDataAccess(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());

    try {
      DATA_ACCESS = new SQLDataAccess(new SQLAuthDAO(), new SQLGameDAO(), new SQLUserDAO());
    } catch (SQLException | DataAccessException e) {
      throw new RuntimeException(e);
    }
  }
  private final UserDAO userDAO = DATA_ACCESS.getUserDAO();
  private final GameDAO gameDAO = DATA_ACCESS.getGameDAO();
  private final AuthDAO authDAO = DATA_ACCESS.getAuthDAO();
  private final Service service = new Service(DATA_ACCESS);
  private final UserData newUser =new UserData("NewUser", "pass2", "user1@email.com");;
  private final UserData existingUser = new UserData("ExistingUser", "pass1", "user12@email.com");
  private final String validAuthToken = "1234";
  private final String invalidAuthToken = "0000";


  @BeforeEach
  public void setUpUser() throws SQLException, DataAccessException {
    service.clear();
    //Add Existing user with valid login
    userDAO.addUser(existingUser);
    authDAO.addAuthData(new AuthData("ExistingUser", validAuthToken));
  }


  @Test
  public void createAuthDataTest() throws SQLException, DataAccessException {
    AuthData authData = service.createAuthData(existingUser, invalidAuthToken);
    assertNotNull(authData);
    assertEquals(invalidAuthToken, authData.authToken());
  }

  @Test
  public void createAuthDataBadTest() throws SQLException, DataAccessException {
    AuthData authData = service.createAuthData(existingUser, validAuthToken);
    authDAO.addAuthData(authData);
    AuthData authData2 = service.createAuthData(existingUser, validAuthToken);
    assertNotNull(authData);
    assertNull(authData2);
  }

  @Test
  public void getAuthDataTest() throws SQLException, DataAccessException {

    AuthRequest authRequest = new AuthRequest("123");
    authDAO.addAuthData(new AuthData("123", "NewUser"));
    AuthData authData = service.getAuthData(authRequest);
    assertEquals("123", authData.authToken());
  }

  @Test
  public void getAuthDataBadTest() throws SQLException, DataAccessException {

    AuthRequest authRequest = new AuthRequest("123");
    AuthData authData = service.getAuthData(authRequest);
    assertNull(authData);
  }

  @Test
  public void registerUserTest() throws SQLException, DataAccessException {

    LoginResult goodResult = service.registerUser(newUser, "123");
    assertEquals(goodResult.getClass(), LoginResult.class);
    assertEquals(goodResult.username(), "NewUser");
    assertEquals(goodResult.authToken(), "123");
  }


  @Test
  public void registerUserTwiceTest() throws SQLException, DataAccessException {
    LoginResult goodResult = service.registerUser(newUser, "123");
    assertEquals(goodResult.getClass(), LoginResult.class);
    assertEquals(goodResult.username(), "NewUser");
    assertEquals(goodResult.authToken(), "123");

    LoginResult badResult = service.registerUser(newUser, "123");
    assertNull(badResult);

    LoginResult badResult2 = service.registerUser(existingUser, "1234");
    assertNull(badResult2);

  }

  @Test
  public void loginRegisteredUserTest() throws SQLException, DataAccessException {
    LoginResult goodResult = service.loginUser(existingUser, "1234");

    assertEquals(goodResult.username(), "ExistingUser");
    assertEquals(goodResult.authToken(), "1234");
  }

  @Test
  public void loginUseNotRegisteredTest() throws SQLException, DataAccessException {
    //shouldn't log in with someone that hasn't been registered
    LoginResult nullResult = service.loginUser(newUser, "123");
    assertNull(nullResult);
  }

  @Test
  public void logoutExistingUserTest() throws SQLException, DataAccessException {
    //Should be able to log out
    AuthRequest logoutRequest = new AuthRequest("1234");
    authDAO.addAuthData(new AuthData("1234", "ExistingUser"));
    LogoutResult goodLogoutResult = service.logoutUser(logoutRequest);

    assertNotNull(goodLogoutResult);
  }

  @Test
  public void logoutNonExistingUserTest() throws SQLException, DataAccessException {
    //Should return null because the authToken is not valid
    AuthRequest logoutRequest = new AuthRequest("000");
    LogoutResult goodLogoutResult = service.logoutUser(logoutRequest);

    assertNull(goodLogoutResult);
  }

  @Test
  public void getGamesTest() throws SQLException, DataAccessException {
    AuthRequest getGamesRequest = new AuthRequest("1234");
    authDAO.addAuthData(new AuthData("1234", "ExistingUser"));

    GetGamesResult getGamesResult = service.getGames(getGamesRequest);
    assertNotNull(getGamesResult);
  }

  @Test
  public void getGamesNotAuthorizedTest() throws SQLException, DataAccessException {
    AuthRequest getGamesRequest = new AuthRequest("000");

    GetGamesResult getGamesResult = service.getGames(getGamesRequest);
    assertNull(getGamesResult);
  }

  @Test
  public void createGamesTest() throws SQLException, DataAccessException {
    AuthRequest authRequest = new AuthRequest(validAuthToken);
    CreateGameRequest createGameRequest = new CreateGameRequest("1");
    authDAO.addAuthData(new AuthData(validAuthToken, "ExistingUser"));

    CreateGameResult createGameResult = service.createGame(authRequest, createGameRequest);
    assertNotNull(createGameResult);
  }

  @Test
  public void createExistingGame() throws SQLException, DataAccessException {
    //Auth not valid to create game
    AuthRequest authRequest = new AuthRequest(invalidAuthToken);
    CreateGameRequest createGameRequest = new CreateGameRequest("1");
    //memoryAuthDAO.addAuthData(new AuthData(validAuthToken,"ExistingUser"));

    CreateGameResult createGameResult = service.createGame(authRequest, createGameRequest);
    assertNull(createGameResult);
  }

  @Test
  public void joinExistingGame() throws SQLException, DataAccessException {
    AuthRequest authRequest = new AuthRequest(validAuthToken);
    JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);

    CreateGameRequest createGameRequest = new CreateGameRequest("1");

    service.createGame(authRequest, createGameRequest);
    JoinGameResult joinGameResult = service.joinGame(authRequest, joinGameRequest);
    assertNotNull(joinGameResult);
  }

  @Test
  public void joinNonExistingGame() throws SQLException, DataAccessException {
    AuthRequest authRequest = new AuthRequest(validAuthToken);
    JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 0);

    CreateGameRequest createGameRequest = new CreateGameRequest("FirsGame_ID_1");

    service.createGame(authRequest, createGameRequest);
    JoinGameResult joinGameResult = service.joinGame(authRequest, joinGameRequest);
    assertNull(joinGameResult.gameData());
  }

  @Test
  public void clear() throws SQLException, DataAccessException {
    ClearResult clearResult = service.clear();
    assertNotNull(clearResult);
  }
}