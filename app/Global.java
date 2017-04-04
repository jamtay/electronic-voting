import models.UserDatabase;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

  /**
   * Initialize the system with different user types.
   */
  public void onStart(Application app) {
    UserDatabase.addUserInfo("John Smith", "smith@example.com", "password", "admin");
    UserDatabase.addUserInfo("James Taylor", "james@example.com", "password", "voter");
    UserDatabase.addUserInfo("James Taylor2", "2james@example.com", "password", "voter");
    UserDatabase.addUserInfo("James Taylor3", "3james@example.com", "password", "voter");
    UserDatabase.addUserInfo("James Taylor4", "4james@example.com", "password", "voter");
    UserDatabase.addUserInfo("James Taylor5", "5james@example.com", "password", "voter");
  }
}
