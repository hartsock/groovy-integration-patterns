package cli.annotations;

import java.lang.annotation.*;

/**
 * @author Shawn Hartsock
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    String name();
}
