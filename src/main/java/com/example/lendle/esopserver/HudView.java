package com.example.lendle.esopserver;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lendle on 2015/1/19.
 */
public class HudView extends ViewGroup {
    private Paint mLoadPaint;
    private long lastClick=-1;
    private GestureDetector gd=null;
    public HudView(Context context) {
        super(context);
        gd=new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                DebugUtils.log("longpress......");
                super.onLongPress(e);
            }
        });
        // TODO Auto-generated constructor stub
        //Toast.makeText(getContext(), "Started", Toast.LENGTH_LONG).show();
        /*mLoadPaint = new Paint();
        mLoadPaint.setAntiAlias(true);
        mLoadPaint.setTextSize(10);
        mLoadPaint.setARGB(255, 255, 0, 0);*/
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawText("Hello World", 5, 15, mLoadPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        DebugUtils.log(""+(ev.getButtonState()==MotionEvent.BUTTON_BACK));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        //Toast.makeText(getContext(),"onTouchEvent", Toast.LENGTH_LONG).show();
        //DebugUtils.log("touch");
        gd.onTouchEvent(event);
        ActivityManager am = (ActivityManager) this.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        String currentActivityClassName=taskInfo.get(0).topActivity.getClassName();

        List <ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();
        boolean pdfRunning=false;
        for(ActivityManager.RunningAppProcessInfo info : l){
            if(info.importance== ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                if(info.processName.contains("andoc")){
                    pdfRunning=true;
                }
            }
        }

        //DebugUtils.log(""+currentActivityClassName+":"+pdfRunning);

        if(currentActivityClassName.contains("andoc")==false){
            return super.onTouchEvent(event);
        }

        long currentTime=System.currentTimeMillis();
        if(lastClick!=-1 && currentTime-lastClick<1000){
            return false;
        }
        lastClick=currentTime;
        DebugUtils.log("touch");

        //assemble information
        Map<String, String> data=new HashMap<String, String>();
        data.put("host", GlobalContext.IP);
        data.put("port", "10001");

        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Point point=new Point();
        wm.getDefaultDisplay().getSize(point);
        final float width=point.x;
        final float x=event.getX();
        final float height=point.y;
        final float y=event.getY();
        data.put("direction", (x>=width/2)?"next":"prev");
        DebugUtils.log("x="+x+", y="+y+", width="+width+", height="+height);
        //back to home if click corner
        if(event.getButtonState()==MotionEvent.BUTTON_BACK){
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(startMain);
            return true;
        }
        /*if(y>=0 && y<=100){
            if( (x>=0 && x<=100) || (x>=width-100) ){
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(startMain);
                return true;
            }
        }else if(y>=height-100){
            if( (x>=0 && x<=100) || (x>=width-100) ){
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(startMain);
                return true;
            }
        }*/

        final String dataString=new Gson().toJson(data);

        new Thread(){
            public void run(){
                try {
                    //multicast

                    InetAddress group = InetAddress.getByName("224.0.0.100");
                    DatagramPacket packet;
                    byte[] buffer = dataString.getBytes();
                    packet = new DatagramPacket(buffer, buffer.length, group, 5001);
                    MulticastSocket socket = new MulticastSocket();
                    socket.send(packet);
                    socket.close();
                    DebugUtils.log(dataString);
                }catch(Exception e){
                    DebugUtils.log(""+e+":"+e.getMessage());
                }
            }
        }.start();

        return true;
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub

    }


}