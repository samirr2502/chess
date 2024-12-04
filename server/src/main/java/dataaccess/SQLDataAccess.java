package dataaccess;


import dataaccess.authdao.AuthDAO;
import dataaccess.gamedao.GameDAO;

import dataaccess.userdao.UserDAO;

import java.sql.SQLException;

public class SQLDataAccess extends DataAccess{
  public SQLDataAccess(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) throws SQLException, DataAccessException {
    super(authDAO, gameDAO,userDAO);
    configureDatabase();

  }


  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS authData (
              `id` int NOT NULL AUTO_INCREMENT,
              `token` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
              
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
          ,

          """
           CREATE TABLE IF NOT EXISTS gameData (
              `id` int NOT NULL AUTO_INCREMENT,
              `gameId` INT NOT NULL,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
          """,
          """
          CREATE TABLE IF NOT EXISTS userData (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
          """
  };
  private void configureDatabase() throws SQLException, DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new SQLException(String.format("Unable to configure database: %s", ex.getMessage()));
    }
  }

}
