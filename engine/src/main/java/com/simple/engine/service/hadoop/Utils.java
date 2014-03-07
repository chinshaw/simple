package com.simple.engine.service.hadoop;

import java.io.InputStream;
import java.io.OutputStream;
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
    
	
	public static void copyStream(InputStream is, OutputStream os)
	 {
	     final int buffer_size=1024;
	     try
	     {
	         byte[] bytes=new byte[buffer_size];
	         for(;;)
	         {
	           int count=is.read(bytes, 0, buffer_size);
	           if(count==-1)
	               break;
	           os.write(bytes, 0, count);
	         }
	     }
	     catch(Exception ex){}
	 }
}
