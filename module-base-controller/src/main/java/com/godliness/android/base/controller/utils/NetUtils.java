package com.godliness.android.base.controller.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by godliness on 2020-04-03.
 *
 * @author godliness
 */
public final class NetUtils {

    public static boolean hasNetwork(Context context){
        if (context != null) {
            final ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                final NetworkInfo info = manager.getActiveNetworkInfo();
                return info != null && info.isAvailable();
            }
        }
        return false;
    }
}
