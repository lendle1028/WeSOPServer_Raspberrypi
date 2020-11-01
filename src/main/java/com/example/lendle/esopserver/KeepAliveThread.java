package com.example.lendle.esopserver;

import android.content.Context;

import com.example.lendle.esopserver.util.SayHelloUtil;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 * Created by lendle on 2015/1/8.
 */
public class KeepAliveThread extends Thread{
    private boolean running=true;
    private Context context=null;

    public KeepAliveThread(Context context) {
        this.context = context;
    }

    public void run(){
        while(running){
            try {
                if(!running){
                    break;
                }
                Thread.sleep((long)(30000+10000*((Math.random()*10000)%3)));
                try {
                    InputStream finishInput = GlobalContext.keepAliveURL.openStream();
                    IOUtils.readLines(finishInput);
                    finishInput.close();
                    //GlobalContext.delayBackUntil=-1;
                } catch (Exception e) {
                    DebugUtils.log("fail to issue keepAlive Packet");
                }
            }catch(Exception e){
                DebugUtils.log(e+":"+e.getMessage());
            }
        }
    }

    public void shutdown(){
        this.running=false;
    }
}
