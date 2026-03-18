# Java 8 New Features Homework

## Question 1
A functional interface is an interface that contains exactly only one abstract method, a concept often referred to as a Single Abstract Method (SAM). While it can only have one abstract method, it is permitted to contain multiple default and static methods. These interfaces are primarily used as the target types for lambda expressions.
Here are two examples:
1. `Predicte<T>`: `boolean test(T t)`; This interface is designed to test a condition. It accepts a single input parameter of type `T` and returns a boolean value based on whether the input matches the criteria.
2. `Consumer<T>`: `void accpet(T t)`; This interface represents an operation that accpets a single input and returns no result. It is typically used for tasks that perform a side effect, such as priting a value to the console or spending an order confirmation email.

## Question 2
```java
interface Greeting {
    default void sayHello() {
        System.out.println("Hello from Greeting");
    }
}
interface Farewell {
    default void sayHello() {
        System.out.println("Hello from Farewell");
    }
}
class Person implements Greeting, Farewell {
    @Override
    public void sayHello() {
        Greeting.super.sayHello();
        System.out.println("And hello from Person");
    }
}
public class Test {
    public static void main(String[] args) {
        Person p = new Person();
        p.sayHello();
    }
}
```
This code will return "Hello from Greeting And hello from Person".
- The `Person` class implements two interfaces, `Greeting` and `Farewall`, both of which provide a `default` implementation for the `sayHello()` method. 
- To resolve the ambiguity, Java requires the implementing class (`Person`) to override the conflicting method. If `Person` did not provide its own `sayHello()` implementation, the code would fail to compile.
- Within the `Person` method, the code uses the specific syntax `Greeting.super.sayHello();`. This instruction tells the JVM bypass the ambiguity by explicitly executing the default implementation defined in the `Greeting` interface. Thus, it prints "Hello from Greeting".
- Then, `System.out.println("And hello from Person");` prints out by `p.sayHello();`.

## Question 3
```java
Comparator comparator = new Comparator() {
    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
};
```
```java
// Lambda Expression
Comparator<String> comparator  = (s1, s2) -> s1.length() - s2.length();
```
1. Remove the unnecessary parts required by the anonymous class syntax, including the class declaration, the method name, and `@Override`.
```java
Comparator<String> comparator = (String s1, String s2) -> { return s1.length() - s2.length(); };
```
2. Remove parameter types since the target type is `Comparator<String>` and `s1` and `s2` must be of type String.
```java 
Comparator<String> comparator = (s1, s2) -> { return s1.length() - s2.length(); };
```
3. Simplify for single statements; Since there is a single statement for return, we can remove the braces.
```java
Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length();
```

## Question 4
```java
// A
Runnable r = () -> System.out.println("Running");
// B
Predicate<String> p = s -> return s.isEmpty();
// C
Function<Integer, Integer> f = x -> { x * 2; };
// D
Consumer<String> c = (String s) -> System.out.println(s);
// E
BiFunction<Integer, Integer, Integer> bi = (a, b) -> a + b;
// F
Supplier<String> sup = () -> { return "Hello"; };
```
- A is valid because `Runnable` is a functional interface with a `void run()` method that takes no parameters.
- B is invalid because it uses a return without braces
- C is invalid because it misses a return keyword inside braces.
- D is valid; `Consumer` has a `void accpet(T t)` method. Using parentheses and explicit type declaration `(String s)` is allowed by lambda syntaax rules.
- E is vaild; `BiFunction` accpets two parameters and returns a result.
- F is valid; `Supplier` takes no parameter and returns a String.

## Question 5
```java
// Lambda expressions:
// 1. x -> System.out.println(x)
// 2. s -> s.toUpperCase()
// 3. x -> Math.abs(x)
// 4. () -> new ArrayList<>()
// 5. (s1, s2) -> s1.compareTo(s2)

// Method references:
// A. ArrayList::new
// B. System.out::println
// C. Math::abs
// D. String::toUpperCase
// E. String::compareTo
```
- (1) -> B: `System.out` is the specific object, and the lambda parameter `x` is passed directly as an argument to its `println` method.
- (2) -> D: This is used when the lambda calls a method on the parameter itself rather than a pre-existing object.
- (3) -> C: The lambda parameter`x`simply becomes the parameter for the static `Math.abs()` method.
- (4) -> A: This syntax is used when the lambda's only purpose is to create a new instance of a class. The `ClassName::new` acts as a shorthand for calling the constructor.
- (5) -> E: The first parameter `s1` becomes the object that calls the method, while the remaining parameter `s2` is passed as an argument to that method.

## Question 6
```java
String value = null;
Optional opt1 = Optional.of(value);
Optional opt2 = Optional.ofNullable(value);
System.out.println(opt1.isPresent());
System.out.println(opt2.isPresent());
```
- `Optional.of(value)` throws `NullPointerException` immediately and uses only when we are 100% sure the value is not null.
- `Optional.ofNullable(value)` returns an empty `Optional` and uses it when a value comes from an external sources.
- The code will throw a `NullPointerException` on the second line and terminate before reaching the print statements. `Optional<String> opt1 = Optional.of(value);` causes the program crash beacuse `Optional.of()` requies a guaranteed non-null value, passing a null value triggers an immediate `NullPointerException`.

## Question 7
```java
public class Test {
    public static String createDefault() {
        System.out.println("Creating default value");
        return "Default";
    }

    public static void main(String[] args) {
        Optional<String> opt = Optional.of("Hello");

        System.out.println("--- Using orElse ---");
        String result1 = opt.orElse(createDefault());
        System.out.println("Result: " + result1);

        System.out.println("--- Using orElseGet ---");
        String result2 = opt.orElseGet(() -> createDefault());
        System.out.println("Result: " + result2);
    }
}
```
- The output of the code will be: `--- Using orElse --- Creating default value Result: Hello --- using orElseGet --- Result: Hello`
- `orElse`: This method takes a plain value as an argument. Because we are passing `createDefault()` as the argument, the Java compiler must execute that method immediately to determain what value to pass into `orElse`. The default value/method is always computeed, event if a value is present.
- `orElseGet` (lazy evaluation): This method takes a `Supplier` rather than a direct value. The logic inside the `Supplier` is only executed if the `Optional` is empty. The default value is computed only if the `Optional` is empty.

## Question 8
```java
class Order {
    private List<Product> products;
    public List<Product> getProducts() { return products; }
}

class Product {
    private String name;
    public String getName() { return name; }
}

// Given: List<Order> orders
// Write code to get: List<String> allProductNames
List<String> allProductNames = order.stream()
    .flatMap(order -> order.getProducts().stream())
    .map(Product::getName)
    .collect(Collectors.toList());
```
- `map()`: This is used for simple transformations where each input element is converted into exactly on output element. If you apply `map()` to a stream of objects and mapping function returns a list, you will end up a nested structure.
- `flatMap()`: This is used when each input element can be transformed into multiple elements. It flattens these nested streams into a single, continuous stream.


## Question 9
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

Stream<Integer> stream = numbers.stream()
    .filter(n -> {
        System.out.println("Filtering: " + n);
        return n % 2 == 0;
    })
    .map(n -> {
        System.out.println("Mapping: " + n);
        return n * 10;
    });

System.out.println("Stream created");
System.out.println("Calling findFirst...");
Optional<Integer> result = stream.findFirst();
System.out.println("Result: " + result.orElse(-1));
```
- The output will be: `Stream created Calling findFirst... Filtering: 1 Filtering 2 Mapping: 2 Result: 20`
- Lazy evaluation in the Stream API means that intermedia operations do not execute at the moment they are defined. Instead, they simply build a "processing pipeline" or a blueprint of the work to be done. The actual processing of data is triggered only when a terminal operation.

## Question 10
```java
public class ProductService {
    public void processProducts(Optional<List<Product>> productsOpt) {
        if (productsOpt.isPresent()) {
            List<Product> products = productsOpt.get();
            for (Product p : products) {
                process(p);
            }
        }
    }

    public Optional<BigDecimal> calculateTotal(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return null;
        }
        BigDecimal total = products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Optional.of(total);
    }
}
```
- In `processProducts(Optional<List<Product>> prodectsOpt)`, the code uses an `Optional` as a parameter. The lecture explicitly states this is a DON'T: you should nece use `Optional` as a parameter. It forces the caller to warp their data in an `Optional`, making the API more complex without providing any real benefit over a simple null check or overloading.
- The code uses `Optional<List<Product>>`. We should never use Optional with collections. A collection already has a natural way to represent "no data" - and empty list. Warpping a list in an `Optional` adds an unnecessary layer of complexity.
- In `calculateTotal`, the code returns `null` if the list is empty. This is a major error because the entire purpose of `Optional` is to prevent `NullPointerExceptions` by providing an explicit "empty box". Returning null from a method that claims to return an `Optional` forces the caller to perform a null check on the `Optional` itself, defeating its purpose.
- `calculateTotal` uses traditioanl manual null and empty checks. While functional, this can be handle more elegently using `Optional` methodes like `Optional.ofNullable()` combined with `filter()`.