package service;


import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.requests.AuthRequest;
import service.results.LoginResult;
import service.results.LogoutResult;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
  private static MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
  private static MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
  private static MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
  private final Service service = new Service();
  private UserData newUser;
  private UserData existingUser;


  @BeforeEach
  public void setUp(){
    service.clear(memoryAuthDAO,memoryUserDAO,memoryGameDAO);
    //New User information with no valid log in (needs to register)
    newUser = new UserData("NewUser","pass1","user1@email.com");
    memoryUserDAO =  new MemoryUserDAO();
    memoryAuthDAO =  new MemoryAuthDAO();
    memoryGameDAO =  new MemoryGameDAO();

    //Add Existing user with valid login
    existingUser = new UserData("ExistingUser","pass1","user1@email.com");
    memoryUserDAO.addUser(existingUser);
    memoryAuthDAO.addAuthData(new AuthData("ExistingUser","123"));
  }
  @Test
  public void registerUserTest(){

    LoginResult goodResult = service.registerUser(newUser, "123", memoryUserDAO,memoryAuthDAO);
    assertEquals(goodResult.getClass(), LoginResult.class);
    assertEquals(goodResult.username(), "NewUser");
    assertEquals(goodResult.authToken(), "123");
  }

  @Test
  public void registerUserTwiceTest(){
    LoginResult goodResult = service.registerUser(newUser, "123", memoryUserDAO,memoryAuthDAO);
    assertEquals(goodResult.getClass(), LoginResult.class);
    assertEquals(goodResult.username(), "NewUser");
    assertEquals(goodResult.authToken(), "123");

    LoginResult badResult = service.registerUser(newUser, "123", memoryUserDAO,memoryAuthDAO);
    assertNull(badResult);

    LoginResult badResult2 = service.registerUser(existingUser,"1234",memoryUserDAO,memoryAuthDAO);
    assertNull(badResult2);

  }
  @Test
  public void loginExistingUserTest(){
    LoginResult goodResult = service.loginUser(existingUser, "1234", memoryUserDAO,memoryAuthDAO);

    assertEquals(goodResult.username(), "ExistingUser");
    assertEquals(goodResult.authToken(), "1234");
  }
  @Test
  public void loginUseNotRegisteredTest(){
    //shouldn't log in with someone that hasn't been registered
    LoginResult nullResult = service.loginUser(newUser, "123", memoryUserDAO,memoryAuthDAO);
    assertNull(nullResult);
  }

  @Test
  public void logoutExistingUserTest(){
    AuthRequest logoutRequest = new AuthRequest("123");
    LogoutResult goodLogoutResult = service.logoutUser(logoutRequest,memoryAuthDAO);

    assertNull(goodLogoutResult);
  }

}
