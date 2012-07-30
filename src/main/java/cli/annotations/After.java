package cli.annotations;

import java.lang.annotation.*;

/**
 * @author Shawn Hartsock
 */
@Documented
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
}
