package com.kirito.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 在信号量上我们定义两种操作：
 * acquire(获取)当一个线程调用acquire操作时，它要么通过成功获取信号量（信号量减一），要么一直等待下去，知道有线程释放信号量，或超时。
 * release(释放)实际上会使信号量加一，然后唤醒等待的线程。
 *
 * 信号量主要作用与两个目的，一个是用于多个共享资源的互斥使用，另一个用于并发线程数的控制。
 * （如果要求多线程抢一个资源，并且要求这个线程持有这个线程20s，就可以用Semaphore！）
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);  //模拟资源类，三个车位

        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"抢占车位");
                    TimeUnit.SECONDS.sleep(4);
                    System.out.println(Thread.currentThread().getName()+"离开了车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
