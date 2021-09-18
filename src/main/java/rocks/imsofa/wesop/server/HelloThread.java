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
                long sleepInterval=5000;
                if(GlobalContext.serverIP!=null){
                    sleepInterval=10000+(long)(Math.random()*5000);
                }
                if(GlobalContext.status==Constants.STATUS_PLAYING){
                    sleepInterval=30000+(long)(Math.random()*5000);
                }
                Thread.sleep(sleepInterval);
            }catch(Exception e){
                DebugUtils.log(e+":"+e.getMessage());
            }
        }
    }

    public void shutdown(){
        this.running=false;
    }
}
