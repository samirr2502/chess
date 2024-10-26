package dataaccess.gamedao;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SQLDataAccess;
import dataaccess.authdao.AuthDAO;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.authdao.SQLAuthDAO;
import dataaccess.userdao.MemoryUserDAO;
import dataaccess.userdao.SQLUserDAO;
import dataaccess.userdao.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {
  private static final DataAccess DATA_ACCESS;

  static {
    //DATA_ACCESS = new MemoryDataAccess(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());

    try {
      DATA_ACCESS = new SQLDataAccess(new SQLAuthDAO(), new SQLGameDAO(), new SQLUserDAO());
    } catch (SQLException | DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private final GameDAO gameDAO = DATA_ACCESS.getGameDAO();
  private final AuthDAO authDAO = DATA_ACCESS.getAuthDAO();
  private final UserDAO userDAO = DATA_ACCESS.getUserDAO();

  private final String validAuthToken = "1234";
  private  final String existingUsernameWhite = "existingUsernameWhite";
  private  final String existingUsernameBlack = "existingUsernameBlack";
  private final String existingGameName1 = "existingGame";
  private final String existingGameName2 = "existingGame2";

  private final ChessGame chessGame= new ChessGame();
  private final int existingGameID = 1;

  @BeforeEach
  void setUp() throws SQLException, DataAccessException {
    gameDAO.deleteAllGames();
    userDAO.deleteAllUsers();
    authDAO.deleteAllAuthData();

    UserData userDataWhite = new UserData(existingUsernameWhite,"ps1","1@email");
    UserData userDataBlack = new UserData(existingUsernameBlack,"ps2","2@email");
    AuthData authDataWhite1 = new AuthData("1234",userDataWhite.username());
    AuthData authDataBlack1 = new AuthData("12345",userDataBlack.username());
    GameData gameData1 =new GameData(1,userDataWhite.username(),userDataBlack.username(),
            existingGameName1,chessGame);
    GameData gameData2 =new GameData(2,userDataWhite.username(),userDataBlack.username(),
            existingGameName2,chessGame);


    userDAO.addUser(userDataWhite);
    userDAO.addUser(userDataBlack);
    authDAO.addAuthData(authDataWhite1);
    authDAO.addAuthData(authDataBlack1);
    gameDAO.addGameData(gameData1);
    gameDAO.addGameData(gameData2);

  }

  @AfterEach
  void tearDown() throws SQLException, DataAccessException {
//    gameDAO.deleteAllGames();
//    userDAO.deleteAllUsers();
//    authDAO.deleteAllAuthData();
  }

  @Test
  void getExistingGameByName() throws SQLException, DataAccessException {
    GameData gameData = gameDAO.getGameByName(existingGameName1);
    assertNotNull(gameData);
  }
  @Test
  void getNotExistingGameByName() throws SQLException, DataAccessException {
    GameData gameData = gameDAO.getGameByName("doesNotExist");
    assertNull(gameData);
  }

  @Test
  void getExistingGameByID() throws SQLException, DataAccessException {
    GameData gameData = gameDAO.getGameByID(1);
    assertNotNull(gameData);
  }
  @Test
  void getNotExistingGameByID() throws SQLException, DataAccessException {
    GameData gameData = gameDAO.getGameByID(0);
    assertNull(gameData);
  }
  @Test
  void getGames() throws SQLException, DataAccessException {
    ArrayList<GameData> games = gameDAO.getGames();
    assertEquals(2,games.size());
  }

  @Test
  void addGameData() throws SQLException, DataAccessException {
    GameData gameData3 =new GameData(3,"whiteUser","blackUser",
            "newGame",new ChessGame());
    if(gameDAO.getGameByName(gameData3.gameName())== null){
      gameDAO.addGameData(gameData3);
    }
    assertNotNull(gameDAO.getGameByName("newGame"));
  }
  @Test
  void addExistingGameData() throws SQLException, DataAccessException {
    GameData gameData2 =new GameData(2,"whiteUser","blackUser",
            existingGameName2,new ChessGame());
    assertNotNull(gameData2);
    if(gameDAO.getGameByName(gameData2.gameName())== null){
      gameDAO.addGameData(gameData2);
    }
    assertNotNull(gameData2);
  }

  @Test
  void updateGame() throws SQLException, DataAccessException {
    GameData gameData2 =new GameData(2,"anotherWhiteUser","blackUser",
            existingGameName2,new ChessGame());
    gameDAO.updateGame(gameData2);
    GameData updatedGameData2 = gameDAO.getGameByName(existingGameName2);
    assertEquals("anotherWhiteUser",updatedGameData2.whiteUsername());
  }

  @Test
  void deleteGameData() throws SQLException, DataAccessException {

    GameData gameData2 = gameDAO.getGameByID(2);

    assertNotNull(gameData2);

    gameDAO.deleteGameData(gameData2);
    GameData gameDataResult = gameDAO.getGameByID(2);
    assertNull(gameDataResult);
  }

  @Test
  void deleteAllGames() throws SQLException, DataAccessException {
    assertNotNull(gameDAO.getGameByID(1));
    assertNotNull(gameDAO.getGameByID(2));

    gameDAO.deleteAllGames();

    assertNull(gameDAO.getGameByID(1));
    assertNull(gameDAO.getGameByID(2));
  }
}