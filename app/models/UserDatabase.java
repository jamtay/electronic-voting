package models;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
  
  private static Map<String, User> users = new HashMap<String, User>();
  
  /**
   * Adds the specified user to the database.
   * @param name The user's name.
   * @param email The user's email.
   * @param password The user's password.
   * @param type The type of user.
   */
  public static void addUserInfo(String name, String email, String password, String type) {
    users.put(email, new User(name, email, password, type));
  }
  
  /**
   * Returns true if the email represents a user who exists.
   * @param email The email.
   * @return True if user exists.
   */
  public static boolean isUser(String email) {
    return users.containsKey(email);
  }

  /**
   * Returns the User associated with the email, or null if not found.
   * @param email The email.
   * @return The User.
   */
  public static User getUser(String email) {
    return users.get((email == null) ? "" : email);
  }

  /**
   * Returns true if email and password are valid credentials.
   * @param email The email. 
   * @param password The password. 
   * @return True if email is a valid user email and password is valid for given email.
   */
  public static boolean isValid(String email, String password) {
    return ((email != null) 
            &&
            (password != null) 
            &&
            isUser(email) 
            &&
            getUser(email).getPassword().equals(password));
  }
}
