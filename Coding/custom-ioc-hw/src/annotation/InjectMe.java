package annotation;

import java.lang.annotation.*;

/*
* Marks a field to be injected automatically by the custom container
* The container will use reflection to locate this field and assign the required dependency object
*/

@Retention(RetentionPolicy.RUNTIME)
public @interface InjectMe {

}
