package com.lbb.lbb.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.cloudinary.utils.ObjectUtils;
import com.lbb.lbb.data.callbacks.ImageUploadCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hitesh on 8/3/17.
 */

public class PhotoManager {

    private static PhotoManager           mInstance;

    Handler                               mHandler;
    private static int                    NUMBER_OF_CORES      = Runtime.getRuntime().availableProcessors();
    private final BlockingQueue<Runnable> mDecodeWorkQueue;
    // Sets the amount of time an idle thread waits before terminating
    private static final int              KEEP_ALIVE_TIME      = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit         KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    ThreadPoolExecutor                    mDecodeThreadPool;

    private PhotoManager() {
        mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>();

        // Creates a thread pool manager
        mDecodeThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES, // Initial pool size
                NUMBER_OF_CORES, // Max pool size
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mDecodeWorkQueue);
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    public static synchronized PhotoManager getInstance() {
        if (mInstance == null)
            mInstance = new PhotoManager();

        return mInstance;
    }

    public void uploadMultiImage(final String[] photoFile, final ImageUploadCallback callback) {
        for (final String photo : photoFile) {

            mDecodeThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Map config = new HashMap();
                    config.put("cloud_name", "hiteshg105");
                    config.put("api_key", "125394391576612");
                    config.put("api_secret", "JQnDITD5vZNyiWCO4tXSJ5XDR7w");
                    com.cloudinary.Cloudinary cloudinary = new com.cloudinary.Cloudinary(config);
                    Log.v("Uploaded", photo + " by " + Thread.currentThread().getName());
                    try {
                        cloudinary.uploader().upload(photo, ObjectUtils.emptyMap());
                        callback.onNext();
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onError();
                    }

                }
            });
        }
    }
}
