package annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface InstancePolicy {
    String value() default "single";
}
