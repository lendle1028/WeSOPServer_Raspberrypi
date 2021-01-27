package rocks.imsofa.wesop.server;

import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
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
            Date startDate = new Date( );
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
                    if (GlobalContext.serverIP != null){
                        DebugUtils.log("recovery for "+ IDUtil.getId()+"......");
                        GlobalContext.serverIP = null;
                        GlobalContext.server.stopServers();
                        Thread.sleep(1000);
                        GlobalContext.server.startServers();
                        recoveryNeeded=false;
                    }
                    else{
                        long startTime = startDate.getTime();
                        long currentTime = new Date().getTime();
                        long diff = currentTime - startTime;
                        if((diff/(1000 * 60)) > 1){
                            DebugUtils.log("network problem for "+ IDUtil.getId()+"......");
                        }
                        Thread.sleep(1000);
                        continue;
                    }
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
