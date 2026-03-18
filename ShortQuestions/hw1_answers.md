# HW1 Short Questions

## Q1
- JVM is Java Virtual Machine, which is the core component that executes Java bytecode. It is responsible for memory management, garbage collection, and providing platform independence, which allows Java programs to run on different operating systems.
- JRE is Java Runtime Environment, which is a package that includes the JVM plus the core class libraries and Java Class Loader. It provides the minimum environment requried to execute Java application.
- JDK is Java Developement Kit, which is the full developement suite. It contains the JRE plus developement tools such as the compiler (javac), the interpreter/loader (java), the archive (jar), documentation generators (javadoc), and debuggers.
- To run a compiled Java program, we only need JRE, as it contains the JVM and necessary libraries to exeecute the code. We would only need the JDK if you were also planning to develop or compile Java source code into bytecode.

## Q2
```java
int a = 10;
int b = a;
b = 20;
System.out.println(a);
```
- It will print 10 because `int b = a` means the actual value held by a is copied inti the new variable `b`. `a` and `b` exist as two separate and independent locations in memory.

## Q3
```java
int[] arr1 = {1, 2, 3};
int[] arr2 = arr1;
arr2[0] = 100;
System.out.println(arr1[0]);
```
- The code will print 100 because `int[] arr2 = arr1` copies the memory address held by `arr1` into `arr2`, any change made through `arr2` is immediately visible when accessing the array through `arr1`.

## Q4
```java
String s1 = "hello";
String s2 = "hello";
String s3 = new String("hello");
System.out.println(s1 == s2);
System.out.println(s1 == s3);
System.out.println(s1.equals(s3));
```
1. `s1 == s2` prints `true` in Java, strings are immutable and are stored in a special memory called the String Constant Pool. When we delare `s1 = "hello"`, the JVM places the string literal in this pool. When `s2 = "hello"` is declared, the JVM reuses the same object already present in the memory address (references) of the objects, the result is true because both variable point to the exact same location in the String Pool.
2. `s1 == s3` prints `false` while `s1` uses the pool, `s3` is created using the `new String("hello")` constructor. The syntax explicitly instructs the JVM to create a new string object in the heap memory, saperete from the String Pool. Since `s1` and `s2` now point to two different objects in different memory locations, the `==` comparison of their references return `false`.
3. `s1.equals(s3)` prints `true` because unlike the `==` operator, the `.equals()` method is used to compare the actual content of the objects. Becasue both `s1` and `s3` contains the identical character sequence "hello", the `.equals()` method returns `true`.

## Q5
- A final variable becomes a constant, meaning its value cannot be reassigned once it has been initialized. If a reference variable is marked `final`, you cannot point it to a new object, but we can still modify the internal contents of that object, such as adding or removing elements from a final list.
- A final method is a method that cannot be overridden by any subclass. This is used when we want to provide a specific implementation of a method that must remain consistent across all child classes, prevending them from changing its behavior.
- A final class is a class that cannot be inherited or extended by any other class. This is primarily used to  ensure security and efficiency by creating immutable classes.

## Q6
- A static variable belongs to the class itself rather than any specific object. In contrast, an instance variable belongs to a specific instance of the class, meaning every object has its own independent copy of that variable. 
- Static variable are stored in the class level, while instance variable are stored in the Heap.
- We can access a static variable using the class name without needing to create an object. Instance variables requir an object reference to be accessed.
- Because static variables are shared, if one object modifies the value, the changes is reflected across all instances of that class. Changes to an instance variable only affect specific object.

Sample example: Counting objects
```java
class User {
    static int userCount = 0;  // shared across all Users
    String name;

    User(String name) {
        this.name = name;
        userCount++;  // increment every time a new User is created
    }
}
```
- userCount is not tied to a specific user
- It represents a global property of the class (total number of users created)
- All instances contribute to the same variable

## Q7
```java
public class Test {
    public static void main(String[] args) {
        final int x = 10;
        x = 20;
        System.out.println(x);
    }
}
```
- The code `final int x = 10;` declared `x` as final, which means it can be assigned once and becomes immutable after initialization. 
- Then, `x = 20;` will be illegal. 
- We will get error that shows cannot assign a value to final variable `x`.

## Q8
1. Encapsulation: This is the process of bundling data and methods that operate on that data into a single unit or class while restricting direct access to internal components to protect data integrity.
2. Inheritance: This establishes an "IS-A" relationship where a new subclass is created from an existing superclass, ingeriting its non-private fields and methods to promote code reusability and extensibility.
3. Polymorphism: Meaning "many forms", this allows onjects to behave differently based on their context, ethier through method overloading at compile-time or method overriding at runtime.
4. Abstraction: This involves hiding complex implementation details by using abstract classes and methods that define the necessary features and "contracts" of an object without specifying the underlying logic.

## Q9
Encapsulation is the process of bundling fields and the methods that operate on that data into a single unit, tipically a class, while restricting direct access to some of the object's components.
We make instance variables private and provide public getter and setter methods because:
- Data Protection and Validation: By making varible private, you prevent outside classes from setting them to invalid values. Public setters allow we to include validation logic.
- Controlled Access: Getters and setters provide a controlled access point to the data. This allows we to define if a field should be ready-only, write-only, or both.
- Flexibility and Implementation Hiding: Encapsulation allows you to change the internal implementation of a class without affecting the parts of the program that use it. For instance, you cloud change how a value is calculated internally, but as long as the oublic getter method remains the same, other classes using it won't break.
- Maintainability: It makes the code easier to maintain and debug because the data is only modifie in specific, predictable ways through the provided methods rather than being changed arbitrarily from anywhere in the program.

## Q10
```java
public class Counter {
    static int count = 0;

    public Counter() {
        count++;
    }

    public static void main(String[] args) {
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        Counter c3 = new Counter();
        System.out.println(Counter.count);
    }
}
```
The output should 3.
- The variable `count` is declared as `static`, which means it belongs to the class itself rather than to any specific instance.
- Every time a new `Counter` object is create (`new Counter()`), the constructor is executed. Inside the constructor, the shared `count` variable is incremented by one.