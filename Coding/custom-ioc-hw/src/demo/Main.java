package demo;

import container.CustomBeanFactory;

import java.util.Set;

/**
 * Main class used to prove the custom IoC container works.
 *
 * This class verifies:
 * 1. Dependency injection works
 * 2. Singleton bean returns the same object
 * 3. Prototype bean returns different objects
 * 4. Multi-level injection also works
 */
public class Main {

    public static void main(String[] args) {

        // Manually register all classes managed by the custom container.
        // This keeps the homework focused on IoC/DI logic instead of package scanning.
        CustomBeanFactory factory = new CustomBeanFactory(Set.of(
                UserRepo.class,
                UserService.class,
                ReportService.class,
                SingletaskManager.class,
                PrototypeTask.class
        ));

        System.out.println("========== 1. Dependency Injection Test ==========");
        UserService userService = factory.getBean(UserService.class);
        userService.printUserInfo();
        System.out.println("Injected repository is null? " + (userService.getUserRepo() == null));

        System.out.println("\n========== 2. Multi-Level Injection Test ==========");
        ReportService reportService = factory.getBean(ReportService.class);
        reportService.generateReport();
        System.out.println("Injected userService is null? " + (reportService.getUserService() == null));

        System.out.println("\n========== 3. Singleton Policy Test ==========");
        SingletaskManager single1 = factory.getBean(SingletaskManager.class);
        SingletaskManager single2 = factory.getBean(SingletaskManager.class);

        System.out.println("single1 = " + single1);
        System.out.println("single2 = " + single2);
        System.out.println("single1 == single2 ? " + (single1 == single2));

        System.out.println("\n========== 4. Prototype Policy Test ==========");
        PrototypeTask proto1 = factory.getBean(PrototypeTask.class);
        PrototypeTask proto2 = factory.getBean(PrototypeTask.class);

        System.out.println("proto1 = " + proto1);
        System.out.println("proto2 = " + proto2);
        System.out.println("proto1 == proto2 ? " + (proto1 == proto2));
    }
}