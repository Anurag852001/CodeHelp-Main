package com.video.CodeHelp;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;

public class MultiThreading {
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();
    static boolean first = true;
    static int turn = 0;
    static final Object object = new Object();

    public static void main(String[] args) {
        List<Thread> threadList = new ArrayList<>();
        for(int i = 0 ;i<20;i++){
            threadList.add(new Thread(makeRunnable(i,false)));
        }


        threadList.parallelStream().forEach(thread->thread.start());
    }

    private static Runnable makeRunnable(int threadNumber, boolean isFirst) {
        return () -> {
            for(int i  = 0;i<1;i++) {
                synchronized (object) {
                    while (turn != threadNumber) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("hello from thread :" + threadNumber);
                    turn = (turn+1)%20;
                    object.notifyAll();
                }
            }
        };
    }
}
