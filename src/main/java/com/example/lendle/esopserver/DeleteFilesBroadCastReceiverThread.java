package com.example.lendle.esopserver;

import android.content.Context;

import com.example.lendle.esopserver.util.PathUtil;
import com.example.lendle.esopserver.util.SayHelloUtil;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by lendle on 2015/6/22.
 */
public class DeleteFilesBroadCastReceiverThread extends Thread {
    private boolean running=true;
    private Context context=null;
    private MulticastSocket socket= null;
    public DeleteFilesBroadCastReceiverThread(Context ctx){
        this.context=ctx;
    }

    public void run(){

        try {
            socket = new MulticastSocket(6001);
            InetAddress group = InetAddress.getByName("224.0.0.100");
            socket.joinGroup(group);
            while(running){
                try{
                    DatagramPacket packet;
                    byte[] buf = new byte[1024];
                    packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    File fileDirectory = new File(PathUtil.getSOPFileFolder().getAbsolutePath());
                    if(fileDirectory.listFiles()!=null) {
                        for (File file : fileDirectory.listFiles()) {
                            FileUtils.deleteQuietly(file);
                        }
                    }
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
