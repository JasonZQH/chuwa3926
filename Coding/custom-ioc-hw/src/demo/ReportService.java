package demo;

import annotation.InjectMe;
import annotation.ManagedComponent;

/**
 * Another service bean to prove that multiple services can
 * receive dependencies from the same container.
 */
@ManagedComponent
public class ReportService {

    @InjectMe
    private UserService userService;

    public void generateReport() {
        System.out.println("ReportService is generating report...");
        userService.printUserInfo();
    }

    public UserService getUserService() {
        return userService;
    }
}