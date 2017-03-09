package views.formdata;

import models.UserDatabase;
import play.data.validation.ValidationError;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for the login form.
 */
public class LoginFormData {


  public String email = "";
  public String password = "";

  /** Constructor needed for form instantiation. */
  public LoginFormData() {
  }

  /**
   * Validates Form<LoginFormData>.
   * Checks to see that email and password are valid credentials.
   * @return Null if valid, or a List[ValidationError] if problems found.
   */
  public List<ValidationError> validate() {

    List<ValidationError> errors = new ArrayList<>();
    
    if (!UserDatabase.isValid(email, password)) {
      errors.add(new ValidationError("email", ""));
      errors.add(new ValidationError("password", ""));      
    }

    return (errors.size() > 0) ? errors : null;
  }

}
