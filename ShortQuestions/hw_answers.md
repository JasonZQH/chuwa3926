# Homework 3 Java OOP

## Question 1
```text
Throwable
├── Error
│   ├── VirtualMachineError
│   │   └── OutOfMemoryError
│   ├── StackOverflowError
│   └── AssertionError
└── Exception
    ├── IOException
    │   ├── FileNotFoundException
    │   └── EOFException
    ├── SQLException
    ├── ClassNotFoundException
    ├── InterruptedException
    └── RuntimeException
        ├── NullPointerException
        ├── ArithmeticException
        ├── IndexOutOfBoundsException
        │   ├── ArrayIndexOutOfBoundsException
        │   └── StringIndexOutOfBoundsException
        ├── IllegalArgumentException
        │   └── NumberFormatException
        └── IllegalStateException
```   
The two main branches under `Throwable` class are `Error` and `Exception`.
- Errors represent serious JVM-level problem that a reasonable application should generally not attempt to catch or handle.
    - `StackOverFlowError`: Occurs when an application recurses too deeply, exhausting the stack frames.
    - `OutOfMemoryError`: Occurs when JVM cannot allocate an object because it is out of memory and no more memory could be more could be made available by hte garbage collector.

- Excaptions are events that disrupt the normal flow of program execution but are recoverable. This branch is futher subdivided into Checked Exceptions and Unchecked Exceptions.
    - `IOException`: A checked exception that when an I/0 operation fails or is interrupted.
    - `NullPointerException`: An unchecked exception that occurs when the program attempts to use an object reference that has an null value.

## Question 2
- Checked exceptions are compile-time exceptions. This means compiler forces you to handle them before the code can be successfully compiled. You must address these exceptions either by wrapping the risky code in a `try-catch` block or by declaring the exception in the method signature using the `throws` keyword. These are typically used for conditions from which an application might reasonably be excepted to recover.
    - `IOException`
    - `FileNotFoundException`

- Unchecked exceptions are also known as Runtime exceptions because they are occur while the program is running. The compiler does not force you to handle or declare them. They typically represent programming errors or bugs rather than environmental failures. While you are not required to catch them, it is still considered good practice to handle them to prevent application crashes.
    - `NullPointerException`
    - `ArrayIndexOutOfBoundsException`

## Question 3
```java
public class ExceptionTest {
    public static int test() {
        try {
            System.out.println("try");
            return 1;
        } catch (Exception e) {
            System.out.println("catch");
            return 2;
        } finally {
            System.out.println("finally");
            return 3;
        }
    }

    public static void main(String[] args) {
        System.out.println(test());
    }
}
```
The code will return `try finally 3`.
- The program enters the `try` block and executes `System.out.println("try")`. It then encounters the `return 1` statement. However, before the value is actually returned to the caller, the `finally` block must execute.
- The program enters the `finally` block and executes `System.out.println("finally")`. It then encounters `return 3`.
- A `return` statement in the `finally` block overrides all other returns from `try` or `catch` blocks. Consequently, the initial attempt to return `1` is discarded, and the method return `3` instead.

## Question 4
- `throw` is used inside of the method body; It actually triggers or "throws" an exception instance; It is an exception instance and can only throw one exception at a time.
- `throws` is used in the method signiture; It declares the possibility that a method mey throw one or more exceptions; It followed by the exception class name and can declare mutiple exceptions separated by commas.

```java
public class ExceptionExample {
    // 'throws' declares that this method might result in an IllegalArgumentException
    public void validateUserAge(int age) throws IllegalArgumentException { 
        if (age < 0) {
            // 'throw' actually creates and triggers the exception instance
            throw new IllegalArgumentException("Age cannot be negative: " + age); 
        }
        System.out.println("Age is valid.");
    }

    public static void main(String[] args) {
        ExceptionExample example = new ExceptionExample();
        try {
            example.validateUserAge(-5);
        } catch (IllegalArgumentException e) {
            System.err.println("Caught error: " + e.getMessage());
        }
    }
}
```

## Question 5
```java
public class Test {
    public static void main(String[] args) {
        try {
            throw new RuntimeException("Error 1");
        } catch (Exception e) {
            throw new RuntimeException("Error 2");
        } finally {
            throw new RuntimeException("Error 3");
        }
    }
}
```
If I run the code, the program will terminate and display `RuntimeException: Error 3`.
- The `try` block executes and throws `RuntimeException("Error 1")`.
- The `catch` block intercepts this exception and attempts to throw a new `RuntimeException("Error 2")`.
- The `finally` blcok must execute before any exception from the `try` or `catch` blocks can propagate out of the method.
- Inside the `finally` block, the code throws `RuntimeException("Error 3")`.

## Question 6
- An Enum cannot extent another class. It is a standard rule in Java that all enums implicitly extend the `java.lang.Enum` class. Because Java does not support multiple inheritance for classes, which means a class can extend only one other class - an enum is prohibited from extending any other class.
- A Enum can implement one or more interfaces. Enums are designed to be powerful tools that can include fields, constructors, and methods. Implementing an interface allows an enum to define a contract for what its constants can do without violating the single-inheritance rule. For practical application, this is often used to implement the Strategy Pattern, where different enum constants provide different implementations of the same interface method.

## Question 7
```java
enum Status {
    PENDING(0),
    PROCESSING(1),
    COMPLETED(2);
    private int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

public class EnumTest {
    public static void main(String[] args) {
        for (Status s : Status.values()) {
            System.out.println(s.name() + " -> " + s.getCode() + " -> " + s.ordinal());
        }
    }
}
```
The custom Enum defines `Status` that includes a private field (`code`), a constructor to initialize that field, and a getter method (`getCode`) to retrieve it. In the `main` method, it uses that built-in `values()` method, which returns an array containing all the constants of the enum in the order thay were declared. Finally, the code prints the name of the constant using the `name()` method, the custom code associated with it via the `getCode()` method, and the position index of the constant using the `ordinal()` method.

Excepted Output: `PENDING -> 0 -> 0 PROCESSING -> 1 -> 1 COMPLETED -> 2 -> 2`
- `s.name()`: This is a standard method that returns the name of the enum constant exactly as it declared in the code.
- `s.getCode()`: This returns the value of the `private int code` field. In this specific code, the values provided to constructor happen to match the index, but they are technically independent values stored wiithin the object.
- `s.ordinal()`: This method returns the ordinal position of the constant, which is its zero-based index in the enum declaration. Since `PENDING` is first, its ordinal is `0`; `PROCESSING` is second, so its ordinal is `1`; and `COMPLETED` is third, resulting in `2`.

## Question 8
| Aspect | Aggregation | Composition |
|-------|-------------|-------------|
| Relationship Type | **HAS-A** (Weak ownership) | **PART-OF** (Strong ownership) |
| Lifecycle | **Independent**; the contained object exists even if the container is destroyed | **Dependent**; the contained object's lifecycle is strictly tied to the container |
| Object Creation Location | **Outside the container**; objects are typically passed into the container as references | **Inside the container**; objects are usually created directly within the container class |
| UML Symbol | Empty diamond ◇ | Filled diamond ◆ |

## Question 9
```java
class Book {
    private String title;
    public Book(String title) { this.title = title; }
}

class Library {
    private List<Book> books = new ArrayList<>();
    public void addBook(Book book) {
        books.add(book);
    }
}

// Usage
Book b1 = new Book("Java Programming");
Library lib = new Library();
lib.addBook(b1);
```
The relationship between `Library` and `Book` in the code is Aggregation.
- In Aggregation, the contained objects are typically created outside the container and passed in as references. In your code, the `Book` object (`b1`) is instantiated independently of the `Library` class before being added via the `addBook` method.
- A key characteristic of Aggregation is the contained object has an independent lifecycle. If the `Library` instance were to be destroyed or set to `null`, the `Book` object would still exist because it was created in the external scope and remains accessible there.
- This is a "HAS-A" relationship. The library has books, but books are not part of the library's physical existence is the way an engine is a part of a car.

## Question 10
The three key elements of the Singleton pattern are:
1. Private static instance variable: A variable within the class that holds the single, unique instance of that class.
2. Private constructor: A constructor that is restricted so that is cannot be accessed from outside the class.
3. Public static `getInstance()` method: A global access point that returns the single instance, creating it first if it does not already exist.

The constructor must be private to prevent the instantiation of the class from external code. By making the constructor private, we ensure that no other class can use the `new` keyword to create additional instances, thereby guaranteeing that only one instance of the class ever exists throughout the application's lifecycle.

## Question 11
The primary difference between Eager Initialization and Lazy Initialization in the Singleton pattern lie in when the object is instantiated and how it handles system resources.

| Aspect | Eager Initialization | Lazy Initialization |
|--------|----------------------|---------------------|
| Creation Time | When the class is loaded | Only when first requested |
| Resource Usage | May waste memory if the instance is never used | Efficient; saves resources by creating objects only when needed |
| Complexity | Simple; usually a single line of code | More complex; requires handling for thread safety |
| Thread Safety | Inherently thread-safe because the JVM handles class loading | Requires specific patterns like "Double-Checked Locking" or a "Bill Pugh" static inner class to be safe |

Eager Initialization creates the instance first. In this approach, the instance is created at the time class is loaded into memory by the JVM. In contrast, Lazy Initialization delays the creation of the instance until the global access point is called for the first time.

## Question 12
```java
public class Singleton {
    private static Singleton instance;
    public Singleton() { }

    public static Singleton getInstance() {
        if (instance == null) {
        instance = new Singleton();
        }
        return instance;
    }
}
```
This implementation is incorrect and fails to guarantee the core requirement of the Singleton pattern: ensuring only one instance of the class exists.
- The most critical error is `public Singleton()`. Becasue the constructor is public, any other class can still use the `new` keyword to create multiple instances of "Singleton", defeating its entire purpose.
- This is a basic Lazy Initialization implementation that is not thread-safe. If two threads call `getInstance()` at the exact same time while the `instance` is still null, both could pass the `if (intance == null)` check simultaneously. This results in each thread creating its own separate instance.

To fix the problems:
- Change the constructor to `private`.
- Add thread-safe: Double-checked Locking, uses the `volatile` keyword to ensure visibility across threads and only locks the code during the first creation.