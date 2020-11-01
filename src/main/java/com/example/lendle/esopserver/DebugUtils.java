package com.example.lendle.esopserver;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.lendle.esopserver.util.MulticastUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lendle on 2014/11/24.
 */
public class DebugUtils {
    public static void showToast(final Context context, final String message) {
        if (Constants.SHOW_DEBUG_TOAST) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(((Activity) context).getBaseContext(), message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.d("com.example.lendle.esopserver", e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void log(String message){
        log(message, Constants.LOG_2_SERVER);
    }

    public static void log(String message, boolean toServer){
        Log.e("com.example.lendle.esopserver", message);
        if(toServer){
            Map<String, String> data=new HashMap<String, String>();
            data.put("host", GlobalContext.IP);
            data.put("port", "10001");
            data.put("message", message);
            data.put("level", "unknown");
            Gson gson=new Gson();

            MulticastUtil.multicast(Constants.MULTICAST_GROUP, 5002, gson.toJson(data));
        }
    }
}
