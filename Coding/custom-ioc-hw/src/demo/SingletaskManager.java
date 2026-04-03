package demo;

import annotation.InstancePolicy;
import annotation.ManagedComponent;

@ManagedComponent
@InstancePolicy("single")
public class SingletaskManager {
}
