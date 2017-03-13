import models.UserDatabase;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

  /**
   * Initialize the system with different user types.
   */
  public void onStart(Application app) {
    UserDatabase.addUserInfo("John Smith", "smith@example.com", "password", "admin");
    UserDatabase.addUserInfo("James Taylor", "james@james.com", "password", "voter");
    UserDatabase.addUserInfo("James Taylor2", "james@james.com2", "password", "voter");
    UserDatabase.addUserInfo("James Taylor3", "james@james.com3", "password", "voter");
    UserDatabase.addUserInfo("James Taylor4", "james@james.com4", "password", "voter");
    UserDatabase.addUserInfo("James Taylor5", "james@james.com", "password", "voter");
  }
}
