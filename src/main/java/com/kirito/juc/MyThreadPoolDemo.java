package com.kirito.juc;

import java.util.concurrent.*;

/**
 * 线程池的七大参数
 * 自定义线程池
 * 四种拒绝策略
 */
public class MyThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService  threadPoolExecutor = new ThreadPoolExecutor(
                2,
                5,
                2l,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                Executors.defaultThreadFactory(),
                //new ThreadPoolExecutor.AbortPolicy());  //(默认)直接抛出RejectedExecutionException异常阻止系统正常运行
                new ThreadPoolExecutor.CallerRunsPolicy()); //“调用者运行”一种调节机制，该策略既不会抛弃任务，也不会抛出异常，而是将某些任务回退到调用者，从而降低新任务的流量。
                //new ThreadPoolExecutor.DiscardOldestPolicy()); //抛弃队列中等待最久的任务，然后把当前任务加人队列中尝试再次提交当前任务
                //new ThreadPoolExecutor.DiscardPolicy());  //DiscardPolicy：该策略默默地丢弃无法处理的任务，不予任何处理也不抛出异常。如果允许任务丢失，这是最好的一种策略。

        try {
            for (int i = 0; i < 9 ; i++) {
                //TimeUnit.SECONDS.sleep(1);
                threadPoolExecutor.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"办理业务");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPoolExecutor.shutdown();
        }
    }

    private static void threadPoolDemo() {
        ExecutorService executorService = Executors.newFixedThreadPool(5); //一池五个线程
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();  //一池一个线程
        ExecutorService executorService2 = Executors.newCachedThreadPool(); //一池N个线程

        try {
            for (int i = 0; i < 10; i++) {
                /*try {
                        TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }*/
                executorService2.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"办理业务");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            executorService2.shutdown();
        }
    }
}
/*
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler) {
    if (corePoolSize < 0 ||
        maximumPoolSize <= 0 ||
        maximumPoolSize < corePoolSize ||
        keepAliveTime < 0)
        throw new IllegalArgumentException();
    if (workQueue == null || threadFactory == null || handler == null)
        throw new NullPointerException();
    this.corePoolSize = corePoolSize;
    this.maximumPoolSize = maximumPoolSize;
    this.workQueue = workQueue;
    this.keepAliveTime = unit.toNanos(keepAliveTime);
    this.threadFactory = threadFactory;
    this.handler = handler;
}
* */