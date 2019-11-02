package com.yrmjhtdjxh.punch.web;

import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        try{

        }finally {
            reentrantLock.unlock();
        }
    }
}
