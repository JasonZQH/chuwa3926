package Coding;

public class Q23_OddEvenPrinterSync {
    private int number = 1;
    private final int max = 10;
    private final Object lock = new Object();

    public void printOdd() {
        synchronized (lock) {
            while (number <= max) {
                while (number % 2 == 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (number <= max) {
                    System.out.println(Thread.currentThread().getName() + ": " + number);
                    number++;
                    lock.notifyAll();
                }
            }
        }
    }

    public void printEven() {
        synchronized (lock) {
            while (number <= max) {
                while (number % 2 == 1) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (number <= max) {
                    System.out.println(Thread.currentThread().getName() + ": " + number);
                    number++;
                    lock.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) {
        Q23_OddEvenPrinterSync printer = new Q23_OddEvenPrinterSync();

        Thread oddThread = new Thread(printer::printOdd, "OddThread");
        Thread evenThread = new Thread(printer::printEven, "EvenThread");

        oddThread.start();
        evenThread.start();
    }
}
