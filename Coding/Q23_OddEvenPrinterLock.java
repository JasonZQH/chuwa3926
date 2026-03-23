package Coding;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Q23_OddEvenPrinterLock {
    private int number = 1;
    private final int max = 10;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void printOdd() {
        lock.lock();
        try {
            while (number <= max) {
                while (number % 2 == 0) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (number <= max) {
                    System.out.println(Thread.currentThread().getName() + ": " + number);
                    number++;
                    condition.signalAll();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void printEven() {
        lock.lock();
        try {
            while (number <= max) {
                while (number % 2 == 1) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (number <= max) {
                    System.out.println(Thread.currentThread().getName() + ": " + number);
                    number++;
                    condition.signalAll();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Q23_OddEvenPrinterLock printer = new Q23_OddEvenPrinterLock();

        Thread oddThread = new Thread(printer::printOdd, "OddThread");
        Thread evenThread = new Thread(printer::printEven, "EvenThread");

        oddThread.start();
        evenThread.start();
    }
}