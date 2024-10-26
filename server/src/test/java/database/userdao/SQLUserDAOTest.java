package database.userdao;

import database.DataAccess;
import database.DataAccessException;
import database.SQLDataAccess;
import database.authdao.AuthDAO;
import database.authdao.SQLAuthDAO;
import database.gamedao.SQLGameDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {
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
  private final String existingUsername = "existingUsername";
  @BeforeEach
  void setUp() throws SQLException, DataAccessException {
    authDAO.deleteAllAuthData();
    userDAO.deleteAllUsers();

    UserData userData = new UserData(existingUsername,"existingPassword","existing@email");
    String validAuthToken = "1234";
    AuthData authData = new AuthData(validAuthToken,userData.username());
    authDAO.addAuthData(authData);
    userDAO.addUser(userData);
  }

  @AfterEach
  void tearDown() throws SQLException, DataAccessException {
    authDAO.deleteAllAuthData();
    userDAO.deleteAllUsers();
  }

  @Test
  void getUser() throws SQLException, DataAccessException {
    UserData userData = userDAO.getUser(existingUsername);
    assertNotNull(userData);
  }
  @Test
  void getNonExistentUser() throws SQLException, DataAccessException {
    UserData userData = userDAO.getUser("notExistent");
    assertNull(userData);
  }

  @Test
  void addExistentUser() throws SQLException, DataAccessException {
    UserData newUserData = new UserData(existingUsername,"password2","email2");
    UserData getUserData = userDAO.getUser(newUserData.username());
    if(getUserData ==null) {
      userDAO.addUser(newUserData);
    }
    assertNotNull(getUserData);
  }

  @Test
  void deleteAllUsers() throws SQLException, DataAccessException {
    UserData userData1 = new UserData("user1","user1pass","user1@emal");
    UserData userData2 = new UserData("user2","user2pass","user2@emal");

    userDAO.addUser(userData1);
    userDAO.addUser(userData2);
    assertNotNull(userDAO.getUser(userData1.username()));
    assertNotNull(userDAO.getUser(userData2.username()));

    userDAO.deleteAllUsers();
    assertNull(userDAO.getUser(userData1.username()));
    assertNull(userDAO.getUser(userData2.username()));

  }
}