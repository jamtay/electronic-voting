package controllers;

import models.Election;
import models.Vote;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.home;
import views.html.voter;
import views.html.login;
import views.formdata.LoginFormData;
import play.mvc.Security;

public class Application extends Controller {

  /**
   * Provides the E-Voting home page.
   * @return The index page.
   */
  public static Result index() {
    return ok(index.render("E-Voting", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
  }
  
  /**
   * Provides the login page to users who are not currently logged in.
   * @return The login page.
   */
  public static Result login() {
    Form<LoginFormData> formData = Form.form(LoginFormData.class);
    return ok(login.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData));
  }

  /**
   * Processes a login form submission from a user who is not currently logged in.
   * If errors are found reload the login page and display errors.
   * If no errors are present then log user in to correct page.
   * @return The index page with the result of login validation.
   */
  public static Result postLogin() {

    // Get the submitted form data from the request object, and run validation.
    Form<LoginFormData> formData = Form.form(LoginFormData.class).bindFromRequest();

    if (formData.hasErrors()) {
      flash("error", "Login credentials not valid.");
      return badRequest(login.render("Login", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData));
    }
    else {
      session().clear();
      session("email", formData.get().email);
      return redirect(routes.Application.home());
    }
  }
  
  /**
   * Logs out and returns user to the index page.
   * @return A redirect to the index page.
   */
  @Security.Authenticated(Secured.class)
  public static Result logout() {
    session().clear();
    return redirect(routes.Application.index());
  }
  
  /**
   * Provides the home page for a logged in user.
   * @return The home page.
   */
  @Security.Authenticated(Secured.class)
  public static Result home() {
    Form<Vote> formData = Form.form(Vote.class).bindFromRequest();
    return ok(home.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData));
  }
}
