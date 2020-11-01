package com.example.lendle.esopserver.util;

import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.GlobalContext;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by lendle on 2015/2/7.
 */
public class MulticastUtil {
    public static void multicast(final String groupIP, final int port, final String message){
        new Thread(){
            public void run(){
                try {
                    //multicast
                    InetAddress group = InetAddress.getByName(groupIP);
                    DatagramPacket packet;
                    byte[] buffer = message.getBytes();
                    packet = new DatagramPacket(buffer, buffer.length, group, port);
                    MulticastSocket socket = new MulticastSocket();
                    socket.send(packet);
                    socket.close();
                    //DebugUtils.log(dataString);
                }catch(Exception e){
                    GlobalContext.helloFailed=true;
                    DebugUtils.log( e + ":" + e.getMessage(), false);
                }
            }
        }.start();
    }
}
