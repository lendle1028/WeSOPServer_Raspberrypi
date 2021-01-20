/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lendle
 */
public abstract class AbstractBroadcastMonitoringThread extends AbstractDaemon {

    protected MulticastSocket socket = null;
    protected int monitoringPort = -1;

    public AbstractBroadcastMonitoringThread(int monitoringPort) {
        try {
            this.monitoringPort = monitoringPort;
            super.setDefaultWaiting(-1);
            socket = new MulticastSocket(monitoringPort);
            InetAddress group = InetAddress.getByName("224.0.0.100");
            socket.joinGroup(group);
        } catch (IOException ex) {
            Logger.getLogger(AbstractBroadcastMonitoringThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void runInBackgroundLoop() {
        try {
            DatagramPacket packet;
            byte[] buf = new byte[1024];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(packet.getData()).trim();
	        //Logger.getLogger(this.getClass().getName()).info(received);
            this.processMessage(received);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected abstract void processMessage(String message);

    public void shutdown() {
        super.shutdown();
        if (this.socket != null) {
            this.socket.close();
        }
    }
}
