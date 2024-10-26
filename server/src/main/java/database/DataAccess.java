package database;

import database.authdao.AuthDAO;
import database.gamedao.GameDAO;
import database.userdao.UserDAO;

public abstract class DataAccess {
  AuthDAO authDAO;
  GameDAO gameDAO;
  UserDAO userDAO;

  public DataAccess(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
    super();
    this.authDAO =  authDAO;
    this.gameDAO = gameDAO;
    this.userDAO = userDAO;
  }

  public AuthDAO getAuthDAO() {
    return authDAO;
  }

  public GameDAO getGameDAO() {
    return gameDAO;
  }

  public UserDAO getUserDAO() {
    return userDAO;
  }
}
