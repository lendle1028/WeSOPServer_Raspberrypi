/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import rocks.imsofa.wesop.server.Constants;
import rocks.imsofa.wesop.server.commands.Command;
import rocks.imsofa.wesop.server.commands.CommandParser;
import rocks.imsofa.wesop.server.ui.server.Global;
import rocks.imsofa.wesop.server.ui.server.OpenRemoteFileAction;

/**
 *
 * @author lendle
 */
public class OpenRemotePressureTestThread extends Thread {

    private long interval = 5000;//milliseconds
    private boolean running = true;
    private String clientIP = null;
    private String filePath = null;

    public OpenRemotePressureTestThread(String clientIP, String filePath, long interval) {
        this.interval = interval;
        this.clientIP = clientIP;
        this.filePath = filePath;
        this.setDaemon(true);
    }

    public void run() {
        while (running) {
            try {
                Thread.sleep(interval);
                try {
                    if (clientIP != null) {
                        OpenRemoteFileAction openRemoteFileAction = new OpenRemoteFileAction();
                        openRemoteFileAction.execute(Global.servletContext, clientIP, Constants.SERVER_PORT, filePath, 1, false);
                        Thread.sleep(interval * 2);
                        final Command command = new Command();
                        command.setGroupName("com.example.lendle.esopserver.commands");
                        command.setName("home");
                        this.sendCommand(clientIP, command);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TesterController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(OpenRemotePressureTestThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void shutdown() {
        this.running = false;
    }

    protected void sendCommand(String clientIP, Command command) {
        try {
            if (command.getParams() == null) {
                command.setParams(new HashMap<String, Object>());
            }
            command.getParams().put("syncTick", "" + System.currentTimeMillis());
            Socket socket = new Socket(clientIP, Constants.SERVER_PORT);
            socket.setSoTimeout(10000);
            IOUtils.write(CommandParser.fromCommand(command), socket.getOutputStream(), "utf-8");
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(TesterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
