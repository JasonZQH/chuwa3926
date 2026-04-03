package annotation;

import java.lang.annotation.*;

/*
* Marks a Class as a managed bean in the custom IOC container
* Only classes annotated with @ManagedComponent will be created
*/
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagedComponent {
    String value() default "";
}
