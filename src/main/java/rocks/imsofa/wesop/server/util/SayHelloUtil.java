package rocks.imsofa.wesop.server.util;

import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import rocks.imsofa.wesop.server.GlobalContext;

/**
 * Created by lendle on 2015/7/27.
 */
public class SayHelloUtil {
    public static void sayHello() throws SocketException {
        //send identity information
        String id= GlobalContext.MACHINE_ID;
        String ip=null;
        //get intranet ip
        Enumeration<NetworkInterface> enumeration=NetworkInterface.getNetworkInterfaces();
        outer: while(enumeration.hasMoreElements()){
            NetworkInterface networkInterface=enumeration.nextElement();
            Enumeration<InetAddress> addresses= networkInterface.getInetAddresses();
            while(addresses.hasMoreElements()){
                InetAddress address=addresses.nextElement();
                //DebugUtils.log(address.getHostAddress());
                if(address.getHostAddress().startsWith("192") || address.getHostAddress().startsWith("10")){
                    ip=address.getHostAddress();
                    break outer;
                }
            }
        }

        //assemble information
        Map<String, String> data=new HashMap<String, String>();
        data.put("id", id);
        data.put("ip", ip);
        data.put("port", "10001");
        data.put("status", GlobalContext.status);
        data.put("downloadingFinishedParts", ""+GlobalContext.downloadingFinishedParts);
        data.put("downloadingTotalParts", ""+GlobalContext.downloadingTotalParts);
        data.put("syncTick", ""+GlobalContext.getSyncTick());
        GlobalContext.IP=ip;
        String dataString=new Gson().toJson(data);
        //multicast
//        System.out.println(dataString);
        MulticastUtil.multicast("224.0.0.100", 5000, dataString);
        GlobalContext.helloFailed=false;
        //DebugUtils.log(dataString);
        //DebugUtils.log("sayingHello!");
    }
}
