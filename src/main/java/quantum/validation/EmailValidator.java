package quantum.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public void initialize(Email contraint) {
    }

    /**
     * Validates that the string is an email.
     *
     * @param value   The value of the string to validate.
     * @param context The context of the validation.
     * @return The boolean value that represents if the value passed the validation or not.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.matches("^(.+)@(.+)$");
    }
}
