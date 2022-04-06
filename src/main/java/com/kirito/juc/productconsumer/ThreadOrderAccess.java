package com.kirito.juc.productconsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程之间按顺序调度：实现 A—>B->C
 * 三个线程启动，要求如下：
 *
 * AA打印5次，BB打印10次，CC打印15次
 * 接着
 * AA打印5次，BB打印10次，CC打印15次
 * 。。。。。来10轮
 */
public class ThreadOrderAccess {
    public static void main(String [] args){
        ShareResource shareResource = new ShareResource();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                shareResource.print(1);
            }
        },"A").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                shareResource.print(2);
            }
        },"B").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                shareResource.print(3);
            }
        },"C").start();
    }
}

class ShareResource{
    private int number=1;  //1 A,2 B,3 C
    private Lock lock=new ReentrantLock();
    private Condition condition1=lock.newCondition();
    private Condition condition2=lock.newCondition();
    private Condition condition3=lock.newCondition();

    public void print(int i){
        try {
            lock.lock();
            while (number!=i){
                if (i==1){
                    condition1.await();
                }else if (i==2){
                    condition2.await();
                }else if (i==3){
                    condition3.await();
                }
            }
            if (i==1){
                number=2;
            }else if (i==2){
                number=3;
            }else if(i==3){
                number=1;
            }
            if (i==1){
                for (int j = 0; j < 5; j++) {
                    System.out.println(Thread.currentThread().getName()+":"+j);
                }
            }else if (i==2){
                for (int j = 0; j < 10; j++) {
                    System.out.println(Thread.currentThread().getName()+":"+j);
                }
            }else if (i==3){
                for (int j = 0; j < 15; j++) {
                    System.out.println(Thread.currentThread().getName()+":"+j);
                }
            }

            if (i==1){
                condition2.signal();
            }else if(i==2){
                condition3.signal();
            }else if (i==3){
                condition1.signal();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }


    public void print5(){
        try {
            lock.lock();
            while (number!=1){
                condition1.await();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName()+":"+i);
            }
            number=2;
            condition2.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void print10(){
        try {
            lock.lock();
            while (number!=2){
                condition2.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName()+":"+i);
            }
            number=3;
            condition3.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void print15(){
        try {
            lock.lock();
            while (number!=3){
                condition3.await();
            }
            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName()+":"+i);
            }
            number=1;
            condition1.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
