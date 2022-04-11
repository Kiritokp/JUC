package com.kirito.juc;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch主要有两个方法，当一个或多个线程调用await方法时，这些线程会阻塞
 * 其他线程调用countDown方法会将计数器减一（调用countDown方法的线程不会阻塞）
 * 当计数器变为零的时，因await方法阻塞的线程会被唤醒，继续执行
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"离开教室");
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"班长关门走人");

    }

    private static void closeDoor() {
        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"离开教室");
            },String.valueOf(i)).start();
        }

        System.out.println(Thread.currentThread().getName()+"班长关门走人");
    }
}
