# Homework 5 Multi-Threading

## Question 1
```
https://www.interviewbit.com/multithreading-interview-questions/#class-level-lock-vs-object-level-lock
```

## Question 2
- A singleton pattern ensoures that a class has only one instance and provides a globel access point to it.

```java
public class Singleton{
    private static volatile Singleton instance;

    private Singleton() {}

    public Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

## Question 3
Creating a new thread in Java can be achieved through traditional direct methods ot by using the more efficient thread pool approach.
- Extending the `Thread` class: Create a subclass of `Thread` and override its `run()` method; `Thread t = new MyThread(); t.start();`
- Implementing the `Runnable` interface: This is often preferred as it allows the class to extend another class if needed. Since `Runnable` is a Functional Interface, you can also use a Lambda expression for more concise implementation.
- Implementing the `Callable` interface: Unlike `Runnable`, `Callable` can return a result and throw checked exceptions. It is used in conjunction with the `Future` interface to retrieve the result.
- For most production applications, using a thread pool via the `ExecutorService` is recommended over creating individual thread manually. You can use the `Executors` factory class to create different types of pools: `newFixedThreadPool(n)`, `newCachedThreadPool()`, and `newSingleThreadExecutor()`.

## Question 4
In Java, both the `Runnable` and `Callable` interfaces are used to encapsulate tasks that are intended to be executed by another thread, but they differ significantly in their capabilities and return types.
- `Runnable` interface uses `run()`. It does not return any result (`void`), which means there is no direct way to get a result back from the thread once it finishes. It cannot throw checked exceptions. And it is introduced in JDK 1.0.
- `Callable` interface uses `call()` and returns a result. When submitted, it returns a `Future` object, you can later call `future.get()` to retrieve the actual value returned by the `call()` method. It can throw checked exceptions. It is introduced in JDK 5.0 (part of the concurrency).

## Question 5
t.start():
- Thread creation: When you call `t.start()`, the JVM creates a newly createed thread.
- Execution: This new thread then proceeds to execute the code contained within the `run()` method.
- Limitation: You can call the `start()` method only once for a specific thread object. Attempting to call it twice will result in an `IllegalThreadStateException`.

t.run():
- No new Thread: Calling `t.run()` does not create a new thread.
- Execution context: The logic inside the `run()` method is executed by the current thread. It behaves linke any other standard method call.
- Frequency: You can call the `run()` method multiple times.
- Result: Because no new thread is created, there is no concurrency; the program waits for the `run()` method to finish before moving to the next line of code.

## Question 6
Implementing the `Runnable` interface is generally considered the better and more flexible way to create threads compared to extending the `Thread` class.
- Inheritance Flexibility: Java does not support multiple inheritance for classes, meaning a class can only extend one superclass. If you extend the `Thread` class, your class cannot extend any other class, but by implementing the `Runnable` interface, your class remains free to extend another class while also having the ability to implement multiple interfaces.
- Support for Lambda Expressions: `Runnable` is a functional interface because it contains exactly one abstract method, which is `run()`. This allows you to use lambda expressions to provide the implementation in a much more concise and readable way than traditional anonymous class approach.
- Separation of Task and Execution: Implementing `Runnable` allows you to separate the task logic from the thread object itself, which is a cleaner design pattern.
- Compatibility with Thread Pools: Modern Java development favors the use of thread pools over manually creating individual threads. Thread pools are designed to execute `Runnable` or `Callable` tasks, and they provide significantly better performance and resource management by resuing existing threads and reducing the overhead creation and destruction.

## Question 7
- NEW: This is the state of a thread that has been newly created as an object but has not yet had its `start()` method called.
- RUNNABLE: This status combines two logical states: ready and running.
    - Ready: After `start()` is called, the thread enters the runnable thread pool and waits to be selected by the thread schedular to receive CPU time.
    - Running: Once the thread is granted a CPU time slice, it moves into the running state to execute its task.
- BLOCKED: In this case: a thread is waiting indefinitly for another thread to perform a specific action, such as a "notify" or "interrupt". This state is triggered by calling methods like `Object.wait()`,`Thread.join()`, or `LockSupport.park()`.
- TIMED_WAITING: This is similar to the WAITING state, but the thread will automatically return after a specific period has elapsed. Common methods that put a thread into this state include `Thread.sleep()`, `Object.wait()`, and `Thread.join()`.
- TERMINATED: This indicates that the thread has finished its execution or has been terminated.

## Question 8
In Java, a dead lock often demonstrated using nested `synchornized` blocks on two different objects:
```java
public class DeadlockDemo {
    // Two resources that need to be locked
    public static final Object LOCK1 = new Object();
    public static final Object LOCK2 = new Object();

    public static void main(String[] args) {
        // Thread A tries to lock Resource 1 then Resource 2
        Thread threadA = new Thread(() -> {
            synchronized (LOCK1) {
                System.out.println("Thread A: Holding LOCK1...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread A: Waiting for LOCK2...");
                synchronized (LOCK2) {
                    System.out.println("Thread A: Holding LOCK1 & LOCK2");
                }
            }
        });

        // Thread B tries to lock Resource 2 then Resource 1
        Thread threadB = new Thread(() -> {
            synchronized (LOCK2) {
                System.out.println("Thread B: Holding LOCK2...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread B: Waiting for LOCK1...");
                synchronized (LOCK1) {
                    System.out.println("Thread B: Holding LOCK2 & LOCK1");
                }
            }
        });

        threadA.start();
        threadB.start();
    }
}
```
To resolve deadlock:
- The simplest resolution is to ensure that all threads always acquire locks in the exact same order. If both Thread A and Thread B were programmed to acquire `LOCK1` first and then `LOCK2`, the second thread would simply wait for the first to finish entirely before starting preventing the circular wait.
- Using `tryLock()` with Timeout: Ttiditional `synchronized` blocks are "heavy" because a thread must wait indefinitely if it cannot acquire the lock. A more flexible resolution is to use the `ReentrantLock` class.
A thread can attempt to acquire a lock but "give up" i it is not available within a specific timeframe. This allows the thread to release its currently held locks and try again later, breaking the deadlock potential.

## Question 9
Threads communicates with each other primarily through a mechanism known as inter-thread communication, which allows multiple threads to coordinate their actions and aviod inefficient practices like thread polling.
- The Wait and Notify Mechanism
    - `wait()`: Causes the current thread to release its lock and go to sleep until another thread involes `notify()` or `notifyAll()` for that same object. This must be called from within a synchornized context.
    - `notify()`: Sends a notification to wake a single thread that waiting on the object's monitor.
    - `notifyAll()`: Sends a notification to wake up all threads waiting on the object's monitor, allowing them to compete for the lock.

- Explicit Locks and Conditions
When using `ReentranLock`, communication is handled via the `Condition` interface.
    - `condition.await()`: The equivalent of `Object.wait()`, which causes the thread to wait.
    - `condition.single()`: The equivalent of `Object.notify()`, waking one waiting thread.
    - `condition.signalAll()`: The equivalent of `Object.notifyAll()`, waking all waiting threads.

- Coordination tools
    - `join()`: This method allows one thread to wait for the completion of another.
    - `Countdown Latch`: Used for collaboration where a set of threads must complete their tasks before a main operation proceeds. Each thread calls `countDown()`, and once the count reaches zero, the waiting thread is released.
    - `BlockingQueue`: A thread-safe queue where a producer thread can `put()` an element and a consumer thread can `take()` it. If the queue is empty, the consumer thread is blocked until the producer inserts an item, facilitating communication through data exchange.

## Question 10
Object Lock is a unique lock assigned to every instance of a class.
- It used to protect non-static data and ensure that only one thread can execute a synchronized non-static method or block on a specific instance of that class at a time.
- You acquire this lock by using the `synchronized` keyword on a non-static method or by using `synchronized(this)` block.
- If you have two different objects of the same class, two different threads can acquire the locks for those objects and execute their synchronized methods simultaneously.

Class Lock is a unique lock shared by all instances of a class.
- It is generally used to make static data thread-safe and to prevent multiple threads from entering a synchronized block across the entire class, regardless of which instance is being used.
- You acquire this lock by using the `static synchronized` keyword on a method or by using a `synchronized(ClassName.class) block.
- Unlike an object lock, a class lock prevents any other thread from entering any order class-locked section of that specific class across the entire JVM.

## Question 11
The `join()` method is a synchronization tool used to coordinate the execution of threads by allowing one thread to wait until another thread has finished its task.
- Thread.join(): When a thread calls `t.join()` on a target thread `t`, the calling thread is paused and remains in a BLOCKED or WAITING state until thread `t` completes. It effeciently 'joins' the start of the calling thread's remaining eexecution to the end of the target thread's execution. This ensures that specific logic only runs after a background task has definitly finished.
- CompleteableFuture.join(): Similar to `Thread.join()`, it blocks the calling thread until the asynchornous task is completed and the result is available. Comparing to `get()`, `Future.get()` is a key advantage of `CompletableFuture.join()` is that it does not require handing checked exceptions, making the code cleaner and easier to use within functional chains.

## Question 12
The `yield()` method is a tool used in multithreading to suggest that the current thread is willing to give up its current use of the CPU to allow other threads to execute.
- When a thread calls `yield()`, it moves from the Running state back to Ready state.
- It is important to note that the thread remains within the broader RUNNABLE status during this transition. It does not become blocked or enter a waiting state; it simply returns to the "runnable thread pool" to wait for the thread schedular to select it again.
- The method serves as a hint to the scheduler that the thread has completed a portion of its task and can be paused to give other threads of equal priority a chance to run.

## Question 13
- A ThreadPool is a collection of pre-initialized worker threads created at application start-up that are used to execute tasks and are returned to the pool once the task is comleted. Here are the types of ThreadPools:
    - FixedThreadPool: A pool with a fixed number of threads. It is ideal for scenarios where you want to limit the total number of concurrent threads.
    - CachedThreadPool: A pool that dynamically adjusts the number of threads based on the workload. It creates new threads as needed but will reuse previously constructed threads when they are available.
    - SingleThreadExecutor: A pool that uses only one worker thead to execute all tasks sequentially. This is useful when tasks must be executed in a specific order.

## Question 14
The library used to create and manage thread pools in Java is the `java.util.concurrent` package. Specifically, the `java.util.concurrent.Executors` factory class provides various methods for creating different types of thread pools, such as fixed, cached, or single-threaded pools.
The main functions and control of a thread pool are provided by the `ExecutorService` interface. This interface is a sub-interface of `Executor` and includes essential methods for managing the lifecycle of asynchronous tasks.

## Question 15
To submite a task to a ThreadPool in Java, we primarily used the methods provided by the `ExectorService` interface. The process generally involves two steps: creating the thread pool and then submitting the task logic via the `submit()` or `execute()` methods.
- `execute(Runnable task)`: This method is used for "fire-and-forgert" tasks. It accpets a `Runnable` and returns `void`.
- `submit(Runnable task)` or `submit(Callable task)`: This is more versatile. It returns a `Future` object, which acts as a placeholder for a result that will be available once the task completes.

For modern asynchornous programming, we can submite tasks without manually handing an `ExecutorService` using `CompletableFuture`. This approach is often preferred because it is non-blocking and supports chaining multiple operations.
- `CompletableFuture.supplyAsync(Supplier supplier)`: Returns a result.
- `CompletableFuture.runAsync(Runnable runnable)`: Not returns a result.

## Question 16
A ThreadPool offers significant advantages over creating individual threads manually, primarily focusing on performance, resource management, and system stability.
- Enhanced Performance and Resource Management: The most immediate benefit of a thread pool is the reduction in overhead. Creating and destroying a thread for every single task is resource-intensive. A thread pool creates a fixed or configurable number of reusable worker threads at startup, allowing them to be shared across many tasks.
- Superior Concurrency Control: Thread pools allow you to limit the number of concurrent tasks being executed at any given time. By configuring the pool size, you ensure the system remains stable under heavy load.
- Efficient Task Queuing: A critical component of the thread pool is the TaskQueue. If all worker threads are currently busy, new tasks are not rejected or forced to create a new thread; instead, they are placed in a queue to wait for the next available thread.
- Automated Lifecycle and Error Management: Using a thread pool shifts the responsibility of lifecycle management from the developer to the JVM. The pool automatically manages when threads are created, how they are assigned tasks, and how they are eventually shut down.

## Question 17
Both `shutdown()` and `shutdownNow()` are methods provided by the `ExecutorService` interface used to manage the ligcycle and termination of a thread pool.
- `shurdown()`: This initiates an orderly shutdown. The executor stops accepting new tasks but continues to process all tasks that ware already submitted, including those currently running and those waiting in the TaskQueue.
- `shutdownNow()`: This attempts to stop the thread pool immediately. It tries to halt all actively executing tasks and ignores any tasks that were waiting in the queue. It also returns a list of the tasks that were never started.

## Question 18
Atomic classes are a set of utility classes in the  `java.util.concurrent.atomic` package that enable lock-free thread safety for variables. They achieve this by using a mechanism called CAS (Compare-and-Swap), which allows multiple threads to updat a shared value without the performance overhead associated with triditional synchronized locks.
There are several high frequently used types:
- `AtomicInteger`: For atomic operations on integer values.
- `AtomicLong`: For atomic operations on long values.
- `AtomicBoolean`: For atomic updates to boolean flags.

Here is an example of `AtomicInteger`:
```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerExample {

    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.incrementAndGet();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final counter: " + counter.get());
    }
}
```

Here is an example for `compareAndSet`:
```java
import java.util.concurrent.atomic.AtomicInteger;

public class CASExample {

    public static void main(String[] args) {
        AtomicInteger value = new AtomicInteger(10);

        boolean success = value.compareAndSet(10, 20);

        System.out.println("Success: " + success);
        System.out.println("New value: " + value.get());
    }
}
```

Atomic classes are ideal for specific high-concurrency scenarios:
- Counters and Accumulators: They are the standard choice for implementing thread-safe counters where the performance cost of `synchronized` blocks would be too high.
- Lambda Expression: Because variables used inside a Lambda must be "effectively final", we cannot use a simple primitive `int` to track state changes. However, because an Atomic object's reference remains the same while its internal state can be mutated, it is a perfect tool for updating totals inside a Lambda loop.
- Low-Level Synchornization: They are used when we need tot ensure a single update occurs across multiple threads without blocking those threads entirely.

## Question 19
Concurrent collections are speciallized, thread-safe versions of standard Java collections designed for use in multi-threaded environments. While standard collections are non-thread-safe and can lead to race conditions, concurrent collections provide built-in synchornization to handle simultaneous access by multiple threads.

Thread-Safe Data Structure:
- `CopyOnWirteArrayList`
- `ConcurrentHashMap`
- `CopyOnWriteArraySet`
- `ArrayBlockingQueue`/`LinkedBlockingQueue`
- `LinkedBlockingDeque`

## Question 20
1. Synchornized
    - Object Lock: Unique to each instance of a class; it protects non-static data.
    - Class Lock: Achieved using `static synchornized`; it protects static data and prevents multiple threads from entering the block across all instances.
- Unlike manual locks, `synchornized` handles excptions automatically; the JVM ensures the lock is released even if an error occurs. Using asychornized block rather than a synchornized method allows you to lock only the "critical section" of code, reducing contention and boosting performance.

2. ReentrantLock
- Unlike `synchornized`, which forces a thread to wait indefinitly, `reentrantLock` allows thread to try to acquire a lock and give up if it is not available within a specific time, helping to aviod deadlocks. Also, the `lock` and `unlock` calls can be placed in different methods, whereas `synchornized` must be contained within a single method.

## Question 21
In Java multithreading, Future and CompletableFuture are interfaces used to manage the results of asynchornous tasks, but they are differ significantly in their execution models.
- A Future represents a result that will be available at a later time after an asynchronous task completes. To retrieve the result, we must call the `get()`; if the task is not yet finished, the calling thread is blocked until the result is ready. It also provides basic methods to check if a task is finished (`isDone()`) or to cancel it (`cancel()`), but it lacks a built-in mechanism for callbacks or chaining multiple tasks together.
- CompletableFuture: It is an advanced implementation of the `Future` interface designed for asynchornous, non-blocking program. Unlike a standard Future, it allows you to provide callback objects that automatically execute once a task is finished or if an execption occurs, meaning the main thread does not have to wait. It supports caching chaining operations, which makes it easy to combine multiple asynchornous tasks into a single processing pipline.