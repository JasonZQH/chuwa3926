package demo;

import annotation.InjectMe;
import annotation.ManagedComponent;

/*
* A service bean that depends on UserRepo
* The dependency should be injected automatically by the factory.
*/
@ManagedComponent
public class UserService {
    
    @InjectMe
    private UserRepo userRepo;

    public void printUserInfo() {
        System.out.println("UserService receicved user: " + userRepo.fetchUserName());
    }

    public UserRepo getUserRepo() {
        return userRepo;
    }

}
