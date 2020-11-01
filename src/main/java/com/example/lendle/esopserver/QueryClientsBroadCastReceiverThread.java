package com.example.lendle.esopserver;

import android.content.Context;
import android.provider.Settings;

import com.example.lendle.esopserver.util.MulticastUtil;
import com.example.lendle.esopserver.util.SayHelloUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lendle on 2015/6/22.
 */
public class QueryClientsBroadCastReceiverThread extends Thread {
    private boolean running=true;
    private Context context=null;
    private MulticastSocket socket= null;
    public QueryClientsBroadCastReceiverThread(Context ctx){
        this.context=ctx;
    }

    public void run(){

        try {
            socket = new MulticastSocket(6000);
            InetAddress group = InetAddress.getByName("224.0.0.100");
            socket.joinGroup(group);
            while(running){
                try{
                    DatagramPacket packet;
                    byte[] buf = new byte[1024];
                    packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    //String received = new String(packet.getData()).trim();
                    SayHelloUtil.sayHello(context);
                    //DebugUtils.log("sayingHello");
                }catch(Exception e){
                    DebugUtils.log(e.toString());
                }
            }
            socket.close();
        } catch (Exception e) {
            DebugUtils.log(e.toString());
        }

    }

    public void shutdown(){
        this.running=false;
        this.interrupt();
        socket.close();
    }
}
