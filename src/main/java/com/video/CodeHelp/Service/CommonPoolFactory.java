package com.video.CodeHelp.Service;

import com.video.CodeHelp.Enums.PoolEnums;

import java.util.concurrent.ForkJoinPool;

import java.util.concurrent.ConcurrentHashMap;


public class CommonPoolFactory {

    private static final ConcurrentHashMap<PoolEnums, ForkJoinPool> poolMap = new ConcurrentHashMap<>();

    public static ForkJoinPool getForkJoinPool(PoolEnums poolEnums) {
        return poolMap.computeIfAbsent(poolEnums, e -> new ForkJoinPool(e.getParallelism()));
    }
}

