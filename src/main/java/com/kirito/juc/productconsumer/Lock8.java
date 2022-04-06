package com.kirito.juc.productconsumer;

import java.util.concurrent.TimeUnit;

/**
 * 题目：多线程8锁
 * 1、标准访问，请问先打印邮件还是短信？           邮件
 * 2、邮件方法暂停4秒，请问先打印邮件还是短信？     邮件
 * 3、新增一个普通方法hello（），请问先打印邮件还是hello（）？     hello（）
 * 4、两部手机，请问先打印邮件还是短信？     短信
 * 5、两个静态同步方法，同一部手机，请问先打印邮件还是短信？   邮件
 * 6、两个静态同步方法，两部手机，请问先打印邮件还是短信？    邮件
 * 7、一个普通方法，一个静态同步方法，一部手机，请问先打印邮件还是短信？  短信
 * 8、一个普通方法，一个静态同步方法，两部手机，请问先打印邮件还是短信？  短信
 */
public class Lock8 {
    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();
        //Phone phone2 = new Phone();
        new Thread(()->{
            phone.sendEmail();
        },"A").start();

        Thread.sleep(100);

        new Thread(()->{
            phone.sendSMS();
            //phone.hello();
            //phone2.sendSMS();
        },"B").start();

    }

}

class Phone{
    public synchronized void sendEmail(){
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("~~~~~~~~sendEmail~~~~~");
    }
    public  synchronized void sendSMS(){
        System.out.println("~~~~~~~~sendSMS~~~~~");
    }
    public void hello(){
        System.out.println("hello!!!");
    }
}
