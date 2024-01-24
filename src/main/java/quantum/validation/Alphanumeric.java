package quantum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = AlphanumericValidator.class)
@Target({FIELD, METHOD, PARAMETER, CONSTRUCTOR})
@Retention(RUNTIME)
public @interface Alphanumeric {

        /**
         * Default validation meesage
         * @return The message to show in case of error.
         */
        String message() default "String must only have letters or numbers.";

        /**
         * Validation message when special characters are allowed.
         * @return The message to show in case of error.
         */
        String specialCharsMessage() default "String must only have letters, numbers or characters '%s'";

        /**
         * Validation message when space characters are not allowed.
         * @return The message to show in case of error.
         */
        String spaceMessage() default "String must not have spaces.";

        /**
         * The special characters allowed.
         * @return The special characters allowed in the validation.
         */
        String allowedChars() default "";

        /**
         * Allow spaces.
         * @return The boolean value that represents if spaces are allowed or not.
         */
        boolean allowSpace() default true;

        /**
         * Required for annotation.
         * @return Empty return.
         */
        Class<?>[] groups() default {};

        /**
         * Required for annotation.
         * @return Empty return.
         */
        Class<? extends Payload>[] payload() default {};
}
