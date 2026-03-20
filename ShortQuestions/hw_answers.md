# Java OOP Homework 2

## Question 1
- Overloading: It is a Compile-Time Polymorphism. Overloaded methods must have a different parameter list, which can vary by the number, type, or order of the argument. It is primariliy used to improve code readability and convenience by allowing the same operation to be performed with different types of input.
- Overriding: It is a Runtime Polymorphism. The overriding method in the subclass must hava the exact same signature as the method in the parent class. It allows a subclass to provide a specific implementation of a behavior that is already  provided by its parent class, enabling features like the Template Method pattern.

## Question 2
```java
class Animal {
    public void makeSound() {
        System.out.println("Some sound");
    }
}

class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Bark!");
    }
}

public class Test {
    public static void main(String[] args) {
        Animal a = new Dog();
        a.makeSound();
    }
}
```
- The output will be `Bark!`. This result is determined by Overriding and Runtime Polymorphism. The `Dog` class provides a specific implementation of the `makeSound()` method that is already defined in its superclass `Animal`. The `@Override` annotation confirms that the subclass is replace the replacing the parent's behavior.

## Question 3
Java does NOT support multiple inheritance with classes primarily to avoid the complexity and ambiguity caused by Diamond Problem.
- The Diamond problem occurs when a single class attempts to inherit from twoo different classes that both contain a method with same signature. If this were allowed, the compiler would have no way to determine which version of the method the child class should inherit ot execute, leading to significant ambiguity.
Java Solution:
- While a class can only extend on superclass, it can implement multiple interfaces.
- With th introduction of default methods in Java 8, it became possible for two interfaces to provide the same method implementation. Java resolves this by forcing the implementing class to override the conflicting method.
- Within the overridden method, a developer can explicitly choose which interface's implementation to use by employing the syntax `InterfaceName.super.methodName()`.

## Question 4
```java
abstract class Shape {
    public abstract double getArea();
}

public class Test {
    public static void main(String[] args) {
        Shape s = new Shape();
        System.out.println(s.getArea());
    }
}
```
The code will show a compile-time error. The code fails to compile because:
- Abstract classes cannot be instantiated: The code attempts to create a new instance of an abstract class with statement `Shape s = new Shape();`. A primary rule of abstract classes is that they cannot be instantiated directly. They are intended to be blueprint for other classes, not objects themselves.
- The `Shape` class contains an abstract method `getArea()`. Abstract methods are declarations without any implementation. Because the method has no code defining how to calculate an area, the JVM would not know what to execute if an instance were allowed to exist.
- While we cannot create a `new Shape()`, we can use an abstract class as a reference type. For the code to work, we would need to create concrete subclass that provides a specific implementation for the `getArea()` method and then instantiate taht subclass instead.

## Question 5
The differences between an abstract class and an interface are rooted in their purpose: an abstract class is used to share a common implementation among closely related classes, while an interface defines a contract or capability that can be shared by any class regardless of its position in the class hierachy.
- A Java class can implement multiple interfaces but can only extend one abstract class.
- Abstract classes can have instance variables to maintain the state of an object, whereas interfaces can only contain public static final constants.
- Abstract classes can have constructors, but interfaces cannot have constructors.
- Abstract classes can have any mix if abstract and concrete methods.
- Interfaces primarily define abstract methods, though since Java 8, they can also include default and static methods.

One scenario I should prefer using an interface is when I want to define a specific capability or contract that needs to be implemented by unrelated classes. Here is an example:
- `Flyable` capability if you have a `Bird` class and an `Airplane` class, they are not closely related in a biological or mechanical hierachy. However, both have ability to fly. If you used an abstract `Animal` class to define a `fly()` method, you would be forced to make `Airplane` a subclass of `Animal`, which is logically incorrect. Instead, by defining a `Flyable` interface, both `Bird` and `Airplane` can implement the `fly()` method independently, allowing them to share common behavior without sharing a common parent class.

## Question 6
```java
class Animal {
    public void eat() {
        System.out.println("Animal eating");
    }
}

class Cat extends Animal {
    public void meow() {
        System.out.println("Meow!");
    }
}

public class Test {
    public static void main(String[] args) {
        Animal a = new Cat(); // Line 1
        a.eat(); // Line 2

        // a.meow(); // Line 3 (commented out)
        if (a instanceof Cat) {
            Cat c = (Cat) a; // Line 4
            c.meow(); // Line 5
        }
    }
}
```
The output of the code will be `Animal eating Meow!`.
1. Upcasting
In Line 1, we are performing upcasting.
- Upcasting is the process of casting a child object (`Cat`) to a parent class reference (`Animal`).
- This is always safe and automatic in Java because a `Cat` IS-A `Animal`.
- When we upcast, we can only access methods defined in the parent class. This is why Line 2 works, but the commented-out Line 3 would cause a compile error.

2. Downcasting (`Cat c = (Cat) a`)
- Downcasting is the process of casting a parent class reference back to a child class type.
- Unlike upcasting, downcasting must be explicit using the `(Type)` syntax.
- Once downcast to a `Cat` reference, we regain access to the child-specific behavior, such as Line 5.

## Question 7
Rules for Overriding equals()
1. For any non-null reference value `x`, `x.equals(x)` must return `true`.
2. For any non-null reference values `x` and `y`, `x.equals(y)` should return `true` if and only if `y.equals(x)` also returns `true`.
3. For any non-null reference values `x`, `y`, and `z`, if `x.equals(y)` is `true` and `y.equals(z)` is `true`, then `x.equals(z)` must also be `true`.
4. Multiple calls to `x.equals(y)` must consistantly return the same result, provided no infomation used in the `equals` comparison on the objects is modified.
5. For any non-null reference value `x`, `x.equals(null)` must always return `false`.

The reson why we must also Override hashCode():
- The fundamental rule is that if two objects are equal according to the `equals(Object)` method, then calling the `hashCode()` method on each of the two objects must produce the same integer result.
- Collections like `HashMap` and `HashSet` rely on the hash code to determine which "bucket" an object should be stored in.
- If two objects are euqal by `equals()` by have different hash codes, a `HashMap` will look in the wrong bucket when you try to retrieve a value using an equal key, resulting in a `null` return even if the key logically exists in the map.
- While different objects can have the same same hash code equal objects must never have different hash codes.

## Question 8
A shallow copy creates a new instance of the class and copies all the field values from the original object to the new one.
- If the `Person` object has a reference to an `Address` object, a shallow copy will only copy the memory address of that `Address`.
- Both the original `Person` and the copied `Person` will point to the exact same `Address` object in memory.
- If we modify the city or street inside the `Address` for the copied `Person`, the change will also appear in the original `Person` because they share the same underlying object.

A deep copy creates a new instance of the class and also creates entirely new copies of an objects referenced by this class.
- In a deep copy, a new `Address` object is instantiated, and its values are copied from the original.
- The orginal `Person` points to original `Address`, while the copied `Person` points ti a completely separate `Address` instance.
- Modifying the `Address` of the copied `Person` will have no effect on the original `Person`.

## Question 9
```java
interface Flyable {
    default void takeOff() {
        System.out.println("Taking off from Flyable");
    }
}

interface Swimmable {
    default void takeOff() {
        System.out.println("Diving in from Swimmable");
    }
}

class Duck implements Flyable, Swimmable {
    @Override
    public void takeOff() {
        Flyable.super.takeOff();
    }
}

public class Test {
    public static void main(String[] args) {
        Duck d = new Duck();
        d.takeOff();
    }
}
```
The output will be `Taking off from Flyable`.
- Both the `Flyable` and `Siwmmable` interface define a default method with the exact same signature: `default void takeOff()`. When the `Duck` class implements both interfaces, a conflict arises because the compiler does not know which default method the `Duck` should inherit.
- To resolve this ambiguity, Java requires the implementing class to explicity override the conflicting method. The `Duck` class fulfills this requirement by providing its own `@override`.
- Inside the overridden method, we have used the specific syntax `Flyable.super.takeOff()`. This syntax tells the JVM to specifically ignore the `Swimmable` version and instead execute the default logic provided by the `Flyable` interface.

## Question 10
```java
public class Calculator {
    public int calculate(int a, int b) {
        return a + b;
    }
    // Method A
    public int calculate(int a, int b, int c) {
        return a + b + c;
    }
    // Method B
    public double calculate(double a, double b) {
        return a + b;
    }
    // Method C
    public double calculate(int a, int b) {
        return (double)(a + b);
    }
    // Method D
    private int calculate(int x, int y) {
        return x * y;
    }
}
```
- Valid Overloads:
1. Method A: It changes the number of the parameters. This satisfies the primary rule for overloading.
2. Method B: It changes the data types of the parameters from `int` to `double`. Even though the method name and the number of parameters are the same, the different types make it a unique signature.

- Invalid Overloads:
1. Method C: Return types are not part of the method signature. Although the return type was changed to double, the name and the parameter type are identical to the original method. Different return types alone are not sufficient to create an overload.
2. Method D: Access modifiers and parameter names are not part of the method signature. Changing the modifier from `public` to `private` or renaming the parameters does not change the signature. Because the parameters are still two integers, this is viewed as a duplicate of the original method. 