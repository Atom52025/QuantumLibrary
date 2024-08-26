package quantum.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AlphanumericValidator implements ConstraintValidator<Alphanumeric, String> {

    private String specialCharsMessage;
    private String spaceMessage;
    private String allowedChars;
    private boolean allowSpace;

    @Override
    public void initialize(Alphanumeric contraint) {
        this.specialCharsMessage = contraint.specialCharsMessage();
        this.spaceMessage = contraint.spaceMessage();
        this.allowedChars = contraint.allowedChars();
        this.allowSpace = contraint.allowSpace();
    }

    /**
     * Validates that the string is alphanumeric (including spaces).
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

        String allowedRegex = "a-zA-Z0-9 ";

        // Remove spaces if not allowed
        if (!allowSpace) {
            allowedRegex = allowedRegex.trim();

            // Return the value here due to the short message of space
            if (value.contains(" ")) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(spaceMessage).addConstraintViolation();
                return false;
            }
        }

        // Add special characters to the regex
        if (allowedChars != null && !allowedChars.isBlank()) {
            allowedRegex += allowedChars;

            // Add custom message for special characters
            context.disableDefaultConstraintViolation();
            String error = String.format(specialCharsMessage, allowedChars.replace("\\", ""));
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        }
        return value.matches("^[" + allowedRegex + "]*$");
    }
}
