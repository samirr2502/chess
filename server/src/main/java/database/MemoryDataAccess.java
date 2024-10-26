package database;


import database.authdao.MemoryAuthDAO;
import database.gamedao.MemoryGameDAO;
import database.userdao.MemoryUserDAO;

public class MemoryDataAccess extends DataAccess {
  MemoryGameDAO UserDAO = new MemoryGameDAO() ;
  MemoryAuthDAO AuthDAO = new MemoryAuthDAO();
  MemoryUserDAO GameDAO = new MemoryUserDAO();

  public MemoryDataAccess(database.authdao.AuthDAO authDAO, database.gamedao.GameDAO gameDAO, database.userdao.UserDAO userDAO) {
    super(authDAO, gameDAO, userDAO);
  }
}
