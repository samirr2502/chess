package dataaccess;

public class MemoryDataAccess extends DataAccess {

  public MemoryDataAccess(dataaccess.authdao.AuthDAO authDAO, dataaccess.gamedao.GameDAO gameDAO, dataaccess.userdao.UserDAO userDAO) {
    super(authDAO, gameDAO, userDAO);
  }
}
