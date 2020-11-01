package com.example.lendle.esopserver;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lendle.esopserver.activities.HudActivity;
import com.example.lendle.esopserver.activities.ScreenActivity;

import java.util.List;

/**
 * Created by lendle on 2015/1/20.
 */
public class HudManagementThread extends Thread {
    private Context context=null;
    private boolean running=true;
    //private HudView hud=null;
    private boolean hudOpened=false;

    public HudManagementThread(Context context){
        this.context=context;
    }


    public void run(){
        while(running){
            try{
                Thread.sleep(100);
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                String currentActivityClassName=taskInfo.get(0).topActivity.getClassName();
                List <ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();
                boolean pdfRunning=false;
                for(ActivityManager.RunningAppProcessInfo info : l){
                    if(info.importance== ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                        //DebugUtils.log(info.processName+":"+currentActivityClassName);
                        if(info.processName.contains("andoc")){
                            pdfRunning=true;
                        }
                    }
                }

                if(pdfRunning && currentActivityClassName.contains("andoc")){
                    //apply hud when adobe reader is on
                    //DebugUtils.log("hud on......");
                    if(GlobalContext.hudActivity==null){
                        new Thread(){
                            public void run(){
//                                try {
//                                    Thread.sleep(5000);
//                                }catch(Exception e){}
                                Intent intent=new Intent(context, HudActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                                DebugUtils.log("hud opened", false);

                                if(com.example.lendle.esopserver.MyActivity.getMainActivity()!=null){
                                    final Activity mainActivity=com.example.lendle.esopserver.MyActivity.getMainActivity();
                                    mainActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            RelativeLayout root= (RelativeLayout) mainActivity.findViewById(R.id.root);
                                            root.setVisibility(View.GONE);
                                        }
                                    });
                                    mainActivity.finish();
                                }

                            }
                        }.start();

                    }
                }else{
                    //remove hud when adobe reader is not on
                    //DebugUtils.log("hud off......");
                    /*if(hudOpened){
                        hudOpened=false;
                    }*/
                }
            }catch(Exception e){
                DebugUtils.log("HudManagementThread: "+e+":"+e.getMessage());
            }
        }
    }

    /*public void run(){
        while(running){
            try{
                Thread.sleep(1000);
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                String currentActivityClassName=taskInfo.get(0).topActivity.getClassName();
                List <ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();
                boolean pdfRunning=false;
                for(ActivityManager.RunningAppProcessInfo info : l){
                    if(info.importance== ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                        //DebugUtils.log(info.processName+":"+currentActivityClassName);
                        if(info.processName.contains("andoc")){
                            pdfRunning=true;
                        }
                    }
                }

                if(pdfRunning && currentActivityClassName.contains("andoc")){
                    //apply hud when adobe reader is on
                    //DebugUtils.log("hud on......");
                    if(this.hud==null){
                        new Thread(){
                            public void run(){
                                Looper.prepare();
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        hud=new HudView(context);

                                        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                                                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                                                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                                                PixelFormat.TRANSLUCENT);
                                        params.gravity = Gravity.RIGHT | Gravity.TOP;
                                        WindowManager wm = (WindowManager) context.getSystemService(android.content.Context.WINDOW_SERVICE);
                                        wm.addView(hud, params);
                                        //Toast.makeText(context, "Hud started", Toast.LENGTH_LONG).show();
                                    }
                                });
                                Looper.loop();
                            }
                        }.start();

                    }
                }else{
                    //remove hud when adobe reader is not on
                    //DebugUtils.log("hud off......");
                    if(this.hud!=null){
                        new Thread(){
                            public void run(){
                                Looper.prepare();
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        removeHud();
                                    }
                                });
                                Looper.loop();
                            }
                        }.start();

                    }
                }
            }catch(Exception e){
                DebugUtils.log("HudManagementThread: "+e+":"+e.getMessage());
            }
        }
    }*/

//    protected void removeHud(){
//        ((WindowManager) context.getSystemService(android.content.Context.WINDOW_SERVICE)).removeView(this.hud);
//        this.hud=null;
//    }

    public void shutdown(){
        DebugUtils.log(""+this.getClass().getName()+": shutdown");
        this.running=false;
//        if(this.hud!=null){
//            removeHud();
//            this.hud=null;
//        }
        this.interrupt();
    }
}
