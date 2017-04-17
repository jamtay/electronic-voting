import models.UserDatabase;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

  /**
   * Initialize the system with different user types.
   */
  public void onStart(Application app) {
    UserDatabase.addUserInfo("John Smith", "admin@example.com", "password", "admin");
    UserDatabase.addUserInfo("James Taylor", "voter1@example.com", "password", "voter");
    UserDatabase.addUserInfo("John Doe", "voter2@example.com", "password", "voter");
    UserDatabase.addUserInfo("Susan Bloggs", "voter3@example.com", "password", "voter");
    UserDatabase.addUserInfo("Jane Smith", "voter4@example.com", "password", "voter");
    UserDatabase.addUserInfo("Chris James", "voter5@example.com", "password", "voter");
  }
}
