package quantum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = EmailValidator.class)
@Target({FIELD, METHOD, PARAMETER, CONSTRUCTOR})
@Retention(RUNTIME)
public @interface Email {

    /**
     * Default validation meesage
     *
     * @return The message to show in case of error.
     */
    String message() default "String must have valid email format.";


    /**
     * Required for annotation.
     *
     * @return Empty return.
     */
    Class<?>[] groups() default {};

    /**
     * Required for annotation.
     *
     * @return Empty return.
     */
    Class<? extends Payload>[] payload() default {};
}
