package com.example.zigdemo;

public class TestThreadStop {
    public static void main(String[] args) {
        Thread3 thread3 = new Thread3();
        thread3.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        thread3.interrupt();
    }
}

class Thread3 extends Thread {
    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Someone interrupted me.");
            } else {
                System.out.println("Thread is Going...");
            }
        }
    }
}
