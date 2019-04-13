package com.ubock.library.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ChenGD on 2018/3/4.
 *
 * @author chenguandu
 */

public class ThreadPools {
    private static final String LOG_TAG = "ThreadPools";

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 4 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "ThreadPools #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    private static final ExecutorService THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    private static final ExecutorService THREAD_POOL_SINGLE_THREAD = Executors.newSingleThreadExecutor();

    public static void execute(Runnable r){
        execute(r,false);
    }

    public static void execute(Runnable r, boolean singleThread){
        if (singleThread){
            if (!THREAD_POOL_SINGLE_THREAD.isShutdown()) {
                THREAD_POOL_SINGLE_THREAD.execute(r);
            }
        } else {
            if (!THREAD_POOL_EXECUTOR.isShutdown()) {
                THREAD_POOL_EXECUTOR.execute(r);
            }
        }
    }
}
