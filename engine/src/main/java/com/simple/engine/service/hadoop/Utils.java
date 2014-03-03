package com.simple.engine.service.hadoop;

import java.util.concurrent.atomic.AtomicLong;

public class Utils {


    /**
     * This is used to create a unique timestamp for things that need
     * a fast unique constant such as filesystem paths.
     * @return
     */
    public static long uniqueCurrentTimeMS() {
        final AtomicLong LAST_TIME_MS = new AtomicLong();
        long now = System.currentTimeMillis();
        while (true) {
            long lastTime = LAST_TIME_MS.get();
            if (lastTime >= now)
                now = lastTime + 1;
            if (LAST_TIME_MS.compareAndSet(lastTime, now))
                return now;
        }
    }
}
