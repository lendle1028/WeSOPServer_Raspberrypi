package rocks.imsofa.wesop.server;

import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import rocks.imsofa.wesop.server.util.IDUtil;
import rocks.imsofa.wesop.server.util.MulticastUtil;

/**
 * Created by lendle on 2015/6/22.
 */
public class CheckNetworkStatusThread extends Thread {
    private boolean running=true;
    private long interval=100;
    /**
     * if back from a network failed state, recovery is needed
     */
    private boolean recoveryNeeded=false;
    public CheckNetworkStatusThread(){
    }

    public void run(){
        while(running){
            try{
                Thread.sleep(interval);
                //DebugUtils.log("server ip: "+GlobalContext.serverIP);
                if(!recoveryNeeded){
                    if(GlobalContext.serverIP==null){
                        //set or reset serverIP
                        String ip=null;
                        //get intranet ip
                        Enumeration<NetworkInterface> enumeration=NetworkInterface.getNetworkInterfaces();
                        outer: while(enumeration.hasMoreElements()){
                            NetworkInterface networkInterface=enumeration.nextElement();
                            Enumeration<InetAddress> addresses= networkInterface.getInetAddresses();
                            while(addresses.hasMoreElements()){
                                InetAddress address=addresses.nextElement();
                                //DebugUtils.log(address.getHostAddress());
                                if(address.getHostAddress().startsWith("192") || address.getHostAddress().startsWith("10.0")){
                                    ip=address.getHostAddress();
                                    break outer;
                                }
                            }
                        }
                        GlobalContext.IP=ip;
                        Map<String, String> data=new HashMap<String, String>();
                        String id= IDUtil.getId();
                        data.put("id", id);
                        data.put("ip", GlobalContext.IP);
                        String dataString=new Gson().toJson(data);
                        MulticastUtil.multicast("224.0.0.100", 5003, dataString);
                    }
                }else{
                    //TODO: re-implement connectivity check
                    //reboot servers, reset central server ip
                    //check network connectivity at first
                    /*ConnectivityManager cm =
                            (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(cm==null){
                        DebugUtils.log("cm=null");
                        Thread.sleep(1000);
                        continue;
                    }
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    if(activeNetwork==null){
                        DebugUtils.log("activeNetwork=null");
                        Thread.sleep(1000);
                        continue;
                    }
                    if(activeNetwork!=null && activeNetwork.isConnected()) {
                        DebugUtils.log("recovery for "+ IDUtil.getId()+"......");
                        GlobalContext.serverIP = null;
                        GlobalContext.server.stopServers();
                        Thread.sleep(1000);
                        GlobalContext.server.startServers();
                        recoveryNeeded=false;
                    }else {
                        continue;
                    }*/
                }

                interval=5000;
                //check network state
                if(!GlobalContext.server.isServerAllOn()){
                    recoveryNeeded=true;
                }
            }catch(Exception e){
                DebugUtils.log(e.toString());
            }
        }
    }

    public void shutdown(){
        this.running=false;
        this.interrupt();
    }
}
