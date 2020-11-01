package com.example.lendle.esopserver.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.Server;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by lendle on 2014/11/24.
 */
public class StartServerTask extends AsyncTask<Void, Void, Void>{
    private Server server=null;
    private Context context=null;

    public StartServerTask(Server server, Context context){
        this.server=server;
        this.context=context;
    }
    @Override
    protected Void doInBackground(Void... params) {
        try {
            server.start();
            final String host = InetAddress.getLocalHost().toString();
            DebugUtils.showToast(context, "server created: " + host);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("com.example.lendle.esopserver", "error:" + e.getMessage());
            DebugUtils.showToast(context, e.getMessage());
        }
        return null;
    }
}
