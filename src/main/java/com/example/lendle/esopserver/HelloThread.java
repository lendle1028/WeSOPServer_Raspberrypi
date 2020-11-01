package com.example.lendle.esopserver;

import android.content.Context;
import android.provider.Settings;

import com.example.lendle.esopserver.util.MulticastUtil;
import com.example.lendle.esopserver.util.SayHelloUtil;
import com.google.gson.Gson;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lendle on 2015/1/8.
 */
public class HelloThread extends Thread{
    private boolean running=true;
    private Context context=null;

    public HelloThread(Context context) {
        this.context = context;
    }

    public void run(){
        while(running){
            try {
                if(!running){
                    break;
                }
                SayHelloUtil.sayHello(context);
                Thread.sleep(5000);
            }catch(Exception e){
                DebugUtils.log(e+":"+e.getMessage());
            }
        }
    }

    public void shutdown(){
        this.running=false;
    }
}
