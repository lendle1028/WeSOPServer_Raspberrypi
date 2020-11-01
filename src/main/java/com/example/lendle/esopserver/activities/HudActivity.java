package com.example.lendle.esopserver.activities;

import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.GlobalContext;
import com.example.lendle.esopserver.activities.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.lendle.esopserver.R;
import com.example.lendle.esopserver.util.MulticastUtil;
import com.example.lendle.esopserver.util.PathUtil;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class HudActivity extends Activity {
    private long lastClick=-1;
    private boolean backPressed=false;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hud);
        GlobalContext.hudActivity=this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.hudActivity=this;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
//    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (AUTO_HIDE) {
//                delayedHide(AUTO_HIDE_DELAY_MILLIS);
//            }
//            return false;
//        }
//    };
//
//    Handler mHideHandler = new Handler();
//    Runnable mHideRunnable = new Runnable() {
//        @Override
//        public void run() {
//            mSystemUiHider.hide();
//        }
//    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
//        mHideHandler.removeCallbacks(mHideRunnable);
//        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        long currentTime=System.currentTimeMillis();
        if(lastClick!=-1 && currentTime-lastClick<1000){
            return false;
        }
        lastClick=currentTime;

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Point point=new Point();
        wm.getDefaultDisplay().getSize(point);
        final float width=point.x;
        final float x=event.getRawX();
        final float height=point.y;
        final float y=event.getY();
        DebugUtils.log("onTouch x="+x+", y="+y+", width="+width+", height="+height);
        //perform flip via flipURL
        final String flipURL= GlobalContext.flipURL+"?host="+GlobalContext.IP+"&port=10001&direction="+((x>=width/2)?"next":"prev");
        new Thread(){
            public void run(){
                try {
                    final URL url=new URL(flipURL);
                    url.openStream();
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugUtils.log("error: "+e+":"+e.getMessage());
                }
            }
        }.start();

        //assemble information
        /*Map<String, String> data=new HashMap<String, String>();
        data.put("host", GlobalContext.IP);
        data.put("port", "10001");
        data.put("direction", (x>=width/2)?"next":"prev");
        DebugUtils.log("x="+x+", y="+y+", width="+width+", height="+height, false);
        final String dataString=new Gson().toJson(data);
        MulticastUtil.multicast("224.0.0.100", 5001, dataString);*/
//        new Thread(){
//            public void run(){
//                try {
//                    //multicast
//
//                    InetAddress group = InetAddress.getByName("224.0.0.100");
//                    DatagramPacket packet;
//                    byte[] buffer = dataString.getBytes();
//                    packet = new DatagramPacket(buffer, buffer.length, group, 5001);
//                    MulticastSocket socket = new MulticastSocket();
//                    socket.send(packet);
//                    socket.close();
//                    //DebugUtils.log(dataString);
//                }catch(Exception e){
//                    DebugUtils.log(""+e+":"+e.getMessage());
//                }
//            }
//        }.start();

        //DebugUtils.log("onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        DebugUtils.log("onBackPressed", false);
        //clean files
        File wesopFolder= PathUtil.getSOPFileFolder();
        File [] files=wesopFolder.listFiles();
        for(File file : files){
            FileUtils.deleteQuietly(file);
        }
        backPressed=true;
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onStop() {
        DebugUtils.log("onstop");
        GlobalContext.hudActivity=null;
        super.onStop();
//        if(this.backPressed==false){
//            Intent intent = new Intent(this, HudActivity.class);
//            startActivity(intent);
//        }
    }
}
