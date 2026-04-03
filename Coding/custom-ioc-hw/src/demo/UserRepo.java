package demo;

import annotation.ManagedComponent;

@ManagedComponent
public class UserRepo {
    
    public String fetchUserName() {
        return "Alice-from-Repo";
    }
}
