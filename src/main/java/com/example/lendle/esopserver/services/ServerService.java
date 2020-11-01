package com.example.lendle.esopserver.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;

import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.GlobalContext;
import com.example.lendle.esopserver.HudManagementThread;
import com.example.lendle.esopserver.HudView;
import com.example.lendle.esopserver.Server;
import com.example.lendle.esopserver.activities.ShowMessageActivity;

import java.io.IOException;

/**
 * Created by lendle on 2015/1/13.
 */
public class ServerService extends Service{
    private Server server=null;
    private HudManagementThread hudManagementThread=null;
    private SequentialTaskThread sequentialTaskThread=null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.hudManagementThread=new HudManagementThread(this);
        this.hudManagementThread.setDaemon(true);
        //this.hudManagementThread.start();
        this.sequentialTaskThread=new SequentialTaskThread();
        this.sequentialTaskThread.start();
        GlobalContext.sequentialTaskThread=this.sequentialTaskThread;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //DebugUtils.log("service started...");
        if(GlobalContext.server==null){
            GlobalContext.server=new Server( this.getApplicationContext());
        }
        server= GlobalContext.server;
        try {
            server.start();
        } catch (IOException e) {
            //server cannot be started, try reboot
            String title= "系統錯誤";
            String message= "系統未能順利啓動，請重新開機";
            Intent i = new Intent(this.getApplicationContext(), ShowMessageActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("message", message);
            i.putExtra("title", title);
            this.getApplicationContext().startActivity(i);
            e.printStackTrace();
            DebugUtils.log(e+":"+e.getMessage());
        }
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
            DebugUtils.log(e+":"+e.getMessage());
        }

        super.onDestroy();
    }

    public void shutdown(){
        try {
            server.stop();
            hudManagementThread.shutdown();
            sequentialTaskThread.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            DebugUtils.log(e+":"+e.getMessage());
        }
    }
}
