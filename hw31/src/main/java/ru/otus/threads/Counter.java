package ru.otus.threads;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class Counter implements Runnable {
    private final int threadNumber;
    private int i = 1;
    private boolean up = true;

    private static final AtomicInteger order = new AtomicInteger(1);

    public Counter(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public static void main(String[] args) {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(new Counter(1)),
                CompletableFuture.runAsync(new Counter(2))
        ).join();

    }

    @Override
    public void run() {
        for (int j = 0; j < 40; j++) {
            count();
            if (i == 1) up = true;
            else if (i == 10) up = false;
            if (up) i++;
            else i--;
        }
    }

    private void count() {
        synchronized (Counter.class) {
            while (order.get() != threadNumber) try {
                Counter.class.wait();
            } catch (InterruptedException e) {
            }
            System.out.println("Thread " + threadNumber + ": " + i);
            Counter.class.notify();
            order.set(order.get() == 1 ? 2 : 1);
        }
    }
}
