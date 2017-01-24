package com.example.dvidr_000.lighthauzproject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by richentra on 24-Nov-16.
 */

public class AppSingleton {
    private static AppSingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private AppSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mRequestQueue,
                    new LruBitmapCache());
        }
    }

    public static synchronized AppSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req,String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


}