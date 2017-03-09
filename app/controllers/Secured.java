package controllers;

import models.User;
import models.UserDatabase;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.Context;

/**
 * Auth controller
 */
public class Secured extends Security.Authenticator {

  /**
   * Used by authentication annotation to determine if user is logged in.
   * Override method to restrict access to home() page for only logged in users
   */
  @Override
  public String getUsername(Context ctx) {
    return ctx.session().get("email");
  }

  /**
   * Override method for Authenticator automatically to redirect to login page if unauthorized
   * user tries to access.
   */
  @Override
  public Result onUnauthorized(Context context) {
    return redirect(routes.Application.login()); 
  }
  
  /**
   * Return the email of the logged in user, or null if no logged in user.
   * Static helper method
   */
  public static String getUser(Context ctx) {
    return ctx.session().get("email");
  }
  
  /**
   * True if there is a logged in user, false otherwise.
   * Static helper method
   */
  public static boolean isLoggedIn(Context ctx) {
    return (getUser(ctx) != null);
  }
  
  /**
   * Return the User of the logged in user, or null if no user is logged in.
   * Static helper method
   */
  public static User getUserInfo(Context ctx) {
    return (isLoggedIn(ctx) ? UserDatabase.getUser(getUser(ctx)) : null);
  }
}