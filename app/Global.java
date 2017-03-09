import models.UserDatabase;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

  /**
   * Initialize the system with different user types.
   */
  public void onStart(Application app) {
    UserDatabase.addUserInfo("John Smith", "smith@example.com", "password", "admin");
    UserDatabase.addUserInfo("James Taylor", "james@james.com", "james", "voter");
  }
}
