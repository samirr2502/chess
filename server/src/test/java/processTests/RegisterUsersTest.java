package processTests;

import com.google.gson.Gson;
import dataaccess.authdao.MemoryAuthDAO;
import dataaccess.gamedao.MemoryGameDAO;
import dataaccess.userdao.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import server.Handler;
import server.Server;
import service.Service;
import spark.Response;
import spark.Request;

public class RegisterUsersTest {
  private static final Server server = new Server();
  private static final MemoryUserDAO memoryUserDAO= new MemoryUserDAO();
  private static final MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
  private static final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
  private static final Gson json= new Gson();
  private Service service;
  private UserData newUser;
  private UserData existingUser;
  private String username;
  private String password;
  private String email;
  @Test
  @BeforeEach
  public void setUp(){

    service = new Service();
    newUser = new UserData(username,password,email);
  }
  @Test
  public void registerUserTest(){
   // service.registerUser(,newUser, memoryUserDAO,memoryAuthDAO);
  }


}
