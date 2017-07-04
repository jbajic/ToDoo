package com.jbajic.todoo.helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jure on 13.05.17..
 * http://www.androidhive.info/2014/05/android-working-with-volley-library-1/
 * https://developer.android.com/training/volley/requestqueue.html
 */

public class VolleyRequestQueue {

    private RequestQueue requestQueue;
    private static VolleyRequestQueue volleyRequestQueue;
    private static  Context context;

    private VolleyRequestQueue(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestQueue getInstance(Context context) {
        if (volleyRequestQueue == null) {
            volleyRequestQueue = new VolleyRequestQueue(context);
        }
        return volleyRequestQueue;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}
