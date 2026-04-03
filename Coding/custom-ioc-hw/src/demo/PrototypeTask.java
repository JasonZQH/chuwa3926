package demo;

import annotation.InstancePolicy;
import annotation.ManagedComponent;

/**
 * A prototype-style bean.
 * Because its policy is "prototype", the factory should create
 * a new object every time getBean() is called.
 */
@ManagedComponent
@InstancePolicy("prototype")
public class PrototypeTask {
}