package com.kirito.juc;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 1、故障现象
 *  java.util.ConcurrentModificationException（并发修改异常）
 *
 * 2、导致原因
 * 多线程高并发
 *
 * 3、解决办法
 *  3.1 Vector()
 *  3.2 Collections.synchronizedList(new ArrayList<>())
 *  3.3 CopyOnWriteArrayList()
 *
 * 4、优化建议（同样的错误，不出现第二次）
 *
 */
public class NoSafeDemo {
    public static void main(String[] args){


    }

    private static void mapNotSafe() {
        Map<String,String> map = new ConcurrentHashMap();
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,8));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }

    private static void setNotSafe() {
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
    }

    private static void listNotSafe() {
        List<String> list=new CopyOnWriteArrayList<>(); //Collections.synchronizedList(new ArrayList<>());//new Vector<>();//new ArrayList<>()
        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}

/*笔记
* 写时复制
CopyOnWrite容器即写时复制的容器。往一个容器添加元素的时候，不直接往当前容器Object[]添加，而是先将当前容器Object[]进行copy，
复制出一个新的容器Object[] newElements,然后在新的容器Object[] newElements里添加元素，添加完元素之后，再将原容器的引用指
向新容器的setArray(newElements);这样的好处是可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。
所以CopyOnWrite容器也是一种读写分离思想，读和写不同的容器。

public boolean add(E e) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        Object[] newElements = Arrays.copyOf(elements, len + 1);
        newElements[len] = e;
        setArray(newElements);
        return true;
    } finally {
        lock.unlock();
    }
}
* */

/*HashSet
HashSet的底层就是HasMap，但是为什么添加元素的时候Hashmap要添加两个(k,v)键值对，而HashSet只添加一个？

HashSet添加元素的add方法调用的就是HashMap的put方法，key是要添加的值，value是Object类型的常量！！

public boolean add(E e) {
        return map.put(e, PRESENT)==null;
}

private static final Object PRESENT = new Object();

*
* */