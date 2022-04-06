package com.kirito.juc.productconsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 题目：现在两个线程，可以操作初始值为0的一个变量，
 * 实现一个线程对该变量+1，一个线程对该变量-1，
 * 实现交替，求10轮，变量初始值为0
 *
 *
 * 1、  高内聚低耦合前提下，线程操作资源类
 * 2、  判断 | 干活 | 通知
 * 3、  多线程交互，必须要防止多线程的虚假唤醒，也即（判断只用while，不能用if）
 * 4、  标志位
 */
public class ThreadWaitNotifyDemo {
    public static void main(String[] args) throws Exception{
        AirConditioner airConditioner=new AirConditioner();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    airConditioner.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"A").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    airConditioner.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"B").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    airConditioner.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"C").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    airConditioner.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"D").start();
    }

}


class AirConditioner{  //资源类

    private int number=0;
    private Lock lock=new ReentrantLock();
    private Condition condition=lock.newCondition();
    public void increment(){
        try {
            lock.lock();
            while (number!=0){
                condition.await();
            }
            number++;
            System.out.println(Thread.currentThread().getName()+":"+number);
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void decrement(){
        try {
            lock.lock();
            while (number==0){
                condition.await();    //await replace wait
            }
            number--;
            System.out.println(Thread.currentThread().getName()+":"+number);
            condition.signalAll();   //signalAll replace notifyAll
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /*public synchronized void increment() throws InterruptedException {
        while (number!=0){   //用if的时候出现错误的原因：多个wait的线程不会再进行条件判断就同时执行后面的代码，而使用while则会重新判断条件
            this.wait();
        }
        number++;
        System.out.println(Thread.currentThread().getName()+":"+number);
        this.notifyAll();
    }

    public synchronized void decrement() throws InterruptedException {
        while (number==0){
            this.wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName()+":"+number);
        this.notifyAll();
    }*/

}
