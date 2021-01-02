package rocks.imsofa.wesop.server.util;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import rocks.imsofa.wesop.server.DebugUtils;
import rocks.imsofa.wesop.server.GlobalContext;

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
                    DebugUtils.log(Level.SEVERE, e + ":" + e.getMessage(), false);
                }
            }
        }.start();
    }
}
