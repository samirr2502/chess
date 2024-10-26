package database;

public class MemoryDataAccess extends DataAccess {

  public MemoryDataAccess(database.authdao.AuthDAO authDAO, database.gamedao.GameDAO gameDAO, database.userdao.UserDAO userDAO) {
    super(authDAO, gameDAO, userDAO);
  }
}
