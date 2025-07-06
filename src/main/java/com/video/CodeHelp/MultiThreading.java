package com.video.CodeHelp;

import java.io.*;
import java.util.concurrent.locks.*;

public class MultiThreading {
    static File file = new File("./sampleFile.txt");
    static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    static Lock readLock = rwLock.readLock();
    static Lock writeLock = rwLock.writeLock();

    public static void main(String[] args) {
        Runnable reader = makeReader();
        Runnable writer = makeWriter();

        Thread r1 = new Thread(reader);
        Thread r2 = new Thread(reader);
        Thread w = new Thread(writer);

        w.start();
        r1.start();
        r2.start();
    }

    private static Runnable makeReader() {
        return () -> {
            readLock.lock();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                System.out.println(Thread.currentThread().getName() + " is reading:");
                while ((line = br.readLine()) != null) {
                    System.out.println(Thread.currentThread().getName() + " read: " + line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                readLock.unlock();
            }
        };
    }

    private static Runnable makeWriter() {
        return () -> {
            writeLock.lock();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("Written by " + Thread.currentThread().getName());
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                writeLock.unlock();
            }
        };
    }
}
