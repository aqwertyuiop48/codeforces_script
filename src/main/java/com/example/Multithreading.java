package main.java.com.example;

public class Multithreading {
    public static void main(String[] args) {

        // *********************************** Example 1 (using Thread()) ***********************************
        Thread t1 = new Thread(() -> {
            for(int i=0;i<5;i++)System.out.println("Hello from thread!");
        });
        t1.start();
        System.out.println("Main thread running");

        // *********************************** Example 2 (Extending Thread) ***********************************
        MyThread t2 = new MyThread();
        t2.start(); // start the new thread
        MyThread t3 = new MyThread();
        t3.start(); // start the new thread

        // *********************************** Example 3 (Implementing Runnable) ***********************************
        Thread t4 = new Thread(new MyRunnable());
        t4.start();

        // *********************************** Example 4 (Thread Synchronization) ***********************************
    }
}

class MyThread extends Thread {
    public void run() {
        System.out.println("Thread running: " + Thread.currentThread().getName());
    }
}

class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Runnable running: " + Thread.currentThread().getName());
    }
}

class Counter {
    private int count = 0;

    public void increment() {
        synchronized(this) {
            count++;
        }
    }

    public int getCount() { return count; }
}

