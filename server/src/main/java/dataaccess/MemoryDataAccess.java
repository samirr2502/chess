package dataaccess;


import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;

public class MemoryDataAccess extends DataAccess {
  MemoryGameDAO UserDAO = new MemoryGameDAO() ;
  MemoryAuthDAO AuthDAO = new MemoryAuthDAO();
  MemoryUserDAO GameDAO = new MemoryUserDAO();

  public MemoryDataAccess(dataaccess.authdao.AuthDAO authDAO, dataaccess.gamedao.GameDAO gameDAO, dataaccess.userdao.UserDAO userDAO) {
    super(authDAO, gameDAO, userDAO);
  }
}
