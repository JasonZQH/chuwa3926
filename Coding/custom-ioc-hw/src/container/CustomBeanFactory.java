package container;

import annotation.InjectMe;
import annotation.InstancePolicy;
import annotation.ManagedComponent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
* Responsebilities:
* 1. Manage bean creation
* 2. Support singleton and prototype instance policies
* 3. Perform field injection for dependencies
*/
public class CustomBeanFactory {
    
    /*
    * Store singleton instances
    * For beans marked as "single", the container creates them once and keeps them in this map
    */
    private final Map<Class<?>, Object> singletonPool = new HashMap<>();

    /*
    * Stores all classes that should be managed by this factory
    * In here, classes are registered manually
    */
    private final Set<Class<?>> managedClasses;

    /*
    * Constructor accepts all managed classes
    * It eagerly creates all singleton beans
    */
    public CustomBeanFactory(Set<Class<?>> managedClasses) {
        this.managedClasses = managedClasses;
        initializeSingletons();
    }

    /*
    * Create all singleton beans during factory initialization
    * Prototype beans are NOT created here
    */
    private void initializeSingletons() {
        for (Class<?> clazz : managedClasses) {
            if (isManagedComponent(clazz) && isSinglePolicy(clazz)) {
                getBean(clazz);
            }
        }
    }

    /*
    * Public API used by application code to retrieve beans
    * If bean policy is "single": return the same instance from singletonPool
    * If bean policy is "prototype": create and return a new instance every time
    */
    public <T> T getBean(Class<T> clazz) {
        if (!isManagedComponent(clazz)) {
            throw new RuntimeException(clazz.getName() + " is not marked with @ManagedComponent");
        }

        if (isSinglePolicy(clazz)) {
            if (!singletonPool.containsKey(clazz)) {
                Object bean = createBean(clazz);
                singletonPool.put(clazz, bean);
            }
            return clazz.cast(singletonPool.get(clazz));
        } else {
            return clazz.cast(createBean(clazz));
        }
    }

    /**
     * Checks whether a class should be managed by the container.
     */
    private boolean isManagedComponent(Class<?> clazz) {
        return clazz.isAnnotationPresent(ManagedComponent.class);
    }

    /*
    * Check whether the bean uses the "single" policy
    * If @InstancePolicy is missing, we treat it as "single" by default.
    */
    private boolean isSinglePolicy(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(InstancePolicy.class)) {
            return true;
        }

        InstancePolicy policy = clazz.getAnnotation(InstancePolicy.class);
        return "Single".equalsIgnoreCase(policy.value());
    }

    /*
    * Creates one bean instance using reflection
    */
    private Object createBean(Class<?> clazz) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            injectDependencies(instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean: " + clazz.getName(), e);
        }
    }

    /*
    * Perform field injection for all fields marked with @InjectMe
    */
    private void injectDependencies(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectMe.class)) {
                Object dependency = getBean(field.getType());

                try {
                    field.setAccessible(true);
                    field.set(bean, dependency);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(
                        "Failed to inject field " + field.getName() + " into " + bean.getClass().getName(), e
                    );
                }
            }
        }
    }

}

