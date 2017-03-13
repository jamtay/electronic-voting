package models;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;


public class Vote {

    public String candidates = "";

    /** Required for form instantiation. */
    public Vote() {
    }

    public Vote(Candidates candidates) {
        this.candidates = candidates.getName();
    }

    public List<ValidationError> validate() {

        List<ValidationError> errors = new ArrayList<>();

        if (candidates == null || candidates.length() == 0) {
            errors.add(new ValidationError("candidates", "No candidate was given."));
        } else if (Candidates.findCandidates(candidates) == null) {
            errors.add(new ValidationError("candidates", "Invalid candidate selected: " + candidates + "."));
        }

        if(errors.size() > 0)
            return errors;

        return null;
    }
}
