package database;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
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
import server.requests.AuthRequest;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import service.results.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

  private final UserDAO userDAO = DATA_ACCESS.getUserDAO();
  private final AuthDAO authDAO = DATA_ACCESS.getAuthDAO();
  private String validToken = "1234";

  @BeforeEach
  public void setUp() throws SQLException, DataAccessException {
    AuthData authData = new AuthData(validToken,"newUser");
    authDAO.addAuthData(authData);

  }
  @Test
  public void addAuthDataTest() throws SQLException, DataAccessException {
    AuthData authData = new AuthData("12345","newAuth");
    authDAO.addAuthData(authData);
    AuthData authDataRet =  authDAO.getAuthDataByToken("12345");
    assertNotNull(authDataRet);
  }
//  @Test
//  public void addAuthDataBadTest() throws SQLException, DataAccessException {
//    AuthData authData = new AuthData("","newAuth");
//    authDAO.addAuthData(authData);
//    AuthData authDataRet =  authDAO.getAuthDataByToken("");
//    assertNull(authDataRet);
//  }
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
}
