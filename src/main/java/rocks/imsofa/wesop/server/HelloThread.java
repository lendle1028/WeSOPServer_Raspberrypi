package rocks.imsofa.wesop.server;

import com.google.gson.Gson;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import rocks.imsofa.wesop.server.util.SayHelloUtil;

/**
 * Created by lendle on 2015/1/8.
 */
public class HelloThread extends Thread{
    private boolean running=true;

    public HelloThread() {
    }

    public void run(){
        while(running){
            try {
                if(!running){
                    break;
                }
                SayHelloUtil.sayHello();
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
