package Coding;

public class Q24_ThreeThreadPrinter {
    static class NumberPrinter implements Runnable {
        private final int start;
        private final int end;

        public NumberPrinter(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new NumberPrinter(1, 10), "Thread-0");
        Thread t2 = new Thread(new NumberPrinter(11, 20), "Thread-1");
        Thread t3 = new Thread(new NumberPrinter(21, 30), "Thread-2");

        t1.start();
        t2.start();
        t3.start();
    }
}
