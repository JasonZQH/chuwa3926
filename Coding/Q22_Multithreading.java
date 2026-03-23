package Coding;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Q22_Multithreading {

    static void demoThreadCreation() throws InterruptedException {
        Thread t1 = new Thread(() -> System.out.println(Thread.currentThread().getName() + " created by Runnable"));
        Thread t2 = new SimpleThread();

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    static class SimpleThread extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " created by extending Thread");
        }

    }

    static void demoSynchronized() throws InterruptedException {
        Counter counter = new Counter();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        
        System.out.println("Final counter = " + counter.getCount());
    }

    static class Counter {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    static void demoWaitNotify() throws InterruptedException {
        MessageQueue queue = new MessageQueue();

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                queue.put("msg-" + i);
                System.out.println("Produced: msg-" + i);
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                String msg = queue.take();
                System.out.println("Consumed: " + msg);
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    static class MessageQueue {
        private final Queue<String> queue = new LinkedList<>();

        public synchronized void put(String message) {
            queue.offer(message);
            notifyAll();
        }

        public synchronized String take() {
            while (queue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return queue.poll();
        }
    }

    static void demoReentrantLock() throws InterruptedException {
        LockerCounter counter = new LockerCounter();

        Runnable task = () -> {
            for (int i = 0; i < 500; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("LockCounter = " + counter.getCount());
    }

    static class LockerCounter {
        private int count = 0;
        private final ReentrantLock lock = new ReentrantLock();

        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }

        public int getCount() {
            return count;
        }
    }

    static void demoExecutorService() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> task1 = () -> {
            Thread.sleep(300);
            return 10;
        };

        Callable<Integer> task2 = () -> {
            Thread.sleep(300);
            return 20;
        };

        Future<Integer> f1 = executor.submit(task1);
        Future<Integer> f2 = executor.submit(task2);

        System.out.println("Future result 1 = " + f1.get());
        System.out.println("Future result 2 = " + f2.get());

        executor.shutdown();
    }

    static void demoFutureAndCompletableFuture() throws Exception {
        CompletableFuture<Integer> sumFuture = CompletableFuture.supplyAsync(() -> 3 + 5);
        CompletableFuture<Integer> productFuture = CompletableFuture.supplyAsync(() -> 3 * 5);

        CompletableFuture<Void> combined =
                sumFuture.thenCombine(productFuture, (sum, product) ->
                        "sum = " + sum + ", product = " + product)
                        .thenAccept(System.out::println);

        combined.get();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("===== 1. Thread Creation =====");
        demoThreadCreation();

        System.out.println("\n===== 2. Synchronized =====");
        demoSynchronized();

        System.out.println("\n===== 3. wait/notify =====");
        demoWaitNotify();

        System.out.println("\n===== 4. ReentrantLock =====");
        demoReentrantLock();

        System.out.println("\n===== 5. ExecutorService =====");
        demoExecutorService();

        System.out.println("\n===== 6. Future / CompletableFuture =====");
        demoFutureAndCompletableFuture();
    }

}