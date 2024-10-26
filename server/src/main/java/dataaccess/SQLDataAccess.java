package dataaccess;


import dataaccess.authdao.AuthDAO;
import dataaccess.gamedao.GameDAO;

import dataaccess.userdao.UserDAO;
import model.AuthData;

import java.sql.SQLException;

public class SQLDataAccess extends DataAccess{
  public SQLDataAccess(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
    super(authDAO, gameDAO,userDAO);
  }
  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS authdata (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
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
      throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
    }
  }
}
