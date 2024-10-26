package database;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SQLDataAccess;
import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.authdao.SQLAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.gamedao.SQLGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.SQLUserDAO;
import dataaccess.userdao.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.results.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
  private static final DataAccess DATA_ACCESS;

  static {
    //DATA_ACCESS = new MemoryDataAccess(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());

    try {
      DATA_ACCESS = new SQLDataAccess(new SQLAuthDAO(), new SQLGameDAO(), new SQLUserDAO());
    } catch (SQLException | DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private final AuthDAO authDAO = DATA_ACCESS.getAuthDAO();
  private String validToken = "1234";

  @BeforeEach
  public void setUp() throws SQLException, DataAccessException {
    authDAO.deleteAllAuthData();
    AuthData authData = new AuthData(validToken,"existing");
    authDAO.addAuthData(authData);

  }
  @Test
  public void addAuthDataTest() throws SQLException, DataAccessException {
    AuthData authData = new AuthData("12345","newAuth");
    authDAO.addAuthData(authData);
    AuthData authDataRet =  authDAO.getAuthDataByToken("12345");
    assertNotNull(authDataRet);
  }
  @Test
  public void addAuthDataBadTest() throws SQLException, DataAccessException {
    AuthData authData = authDAO.getAuthDataByToken(validToken);
    if (authData == null) {
      authData = new AuthData("000","shouldNotBeHere");
      authDAO.addAuthData(authData);
    }
    assertNotNull(authData);
  }
  @Test
  public void getAuthByTokenGoodTest() throws SQLException, DataAccessException {
    AuthData authData =  authDAO.getAuthDataByToken("1234");
    assertNotNull(authData);
  }
  @Test
  public void getAuthByTokenBadTest() throws SQLException, DataAccessException {
    AuthData authData =  authDAO.getAuthDataByToken("123");
    assertNull(authData);
  }
  @Test
  public void deleteAuthByTokenGoodTest() throws SQLException, DataAccessException {
    AuthData authData = new AuthData("1234","newUser");
    assertNotNull(authData);
    authDAO.deleteAuthData(authData);
    AuthData authDataResult = authDAO.getAuthDataByToken(validToken);
    assertNull(authDataResult);
  }
  @Test
  public void deleteAuthByTokenBadTest() throws SQLException, DataAccessException {
    AuthData authData =  authDAO.getAuthDataByToken("123");
    if (authData!= null) {
      authDAO.deleteAuthData(authData);
    }
    assertNull(authData);
  }

  @Test
  public void deleteAllAuthData() throws SQLException, DataAccessException {
    AuthData authData1 = new AuthData("123456","newUser1");
    AuthData authData2 = new AuthData("1234567","newUser2");
    authDAO.addAuthData(authData1);
    authDAO.addAuthData(authData2);
    assertNotNull(authDAO.getAuthDataByToken("123456"));
    assertNotNull(authDAO.getAuthDataByToken("1234567"));

    authDAO.deleteAllAuthData();
    assertNull(authDAO.getAuthDataByToken("123456"));
    assertNull(authDAO.getAuthDataByToken("1234567"));

  }


}
