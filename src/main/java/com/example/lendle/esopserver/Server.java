package com.example.lendle.esopserver;

import android.content.Context;
import android.util.Log;

import com.example.lendle.esopserver.commands.BackHomeCommandExecutor;
import com.example.lendle.esopserver.commands.Command;
import com.example.lendle.esopserver.commands.CommandDispatcher;
import com.example.lendle.esopserver.commands.CommandParser;
import com.example.lendle.esopserver.commands.FileCopyCommandExecutor;
import com.example.lendle.esopserver.commands.OpenPDFCommandExecutor;
import com.example.lendle.esopserver.commands.OpenRemoteFileCommandExecutor;
import com.example.lendle.esopserver.commands.RestartPackageCommandExecutor;
import com.example.lendle.esopserver.commands.ScreenCommandExecutor;
import com.example.lendle.esopserver.commands.SetServerIPCommandExecutor;
import com.example.lendle.esopserver.commands.SetStatusCommandExecutor;
import com.example.lendle.esopserver.commands.ShowMessageCommandExecutor;
import com.example.lendle.esopserver.commands.TerminalCommandExecutor;
import com.example.lendle.esopserver.commands.TestCommandExecutor;
import com.example.lendle.esopserver.commands.TestFileCopyCommandExecutor;
import com.example.lendle.esopserver.commands.TestOpenPDFCommandExecutor;
import com.example.lendle.esopserver.commands.acrobat.PDFFlipCommandExecutor;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lendle on 2014/10/20.111
 */
public class Server {
    private ServerSocket serverSocket = null;
    private ServerThread serverThread = null;
    private boolean running = false;
    private Context context = null;
    private CommandDispatcher commandDispatcher=null;
    private HelloThread helloThread=null;
    private CheckNetworkStatusThread checkNetworkStatusThread =null;
    private QueryClientsBroadCastReceiverThread queryClientsBroadCastReceiverThread=null;
    private DeleteFilesBroadCastReceiverThread deleteFilesBroadCastReceiverThread=null;
    private CheckDiskspaceStatusThread checkDiskspaceStatusThread=null;
    private KeepAliveThread keepAliveThread=null;

    public Server(Context context) {
        this.context = context;
        this.commandDispatcher=new CommandDispatcher();
        this.setupCommandDispatcher();
    }

    public void start() throws IOException {
        if (serverSocket == null && running == false) {
//            try {
//                Log.e("com.example.lendle.esopserver", "test.sh");
//                ProcessUtil.execute("su -c /mnt/sdcard/test.sh", false);
//                Thread.sleep(500);
//
//                Log.e("com.example.lendle.esopserver", "su");
//                ProcessUtil.execute("su", false);
//                Thread.sleep(500);
//                Log.e("com.example.lendle.esopserver", "setprop");
//                ProcessUtil.execute("su -c setprop service.adb.tcp.port 5555", false);
//                Thread.sleep(500);
//                Log.e("com.example.lendle.esopserver", "killall");
//                ProcessUtil.execute("su -c killall adbd", false);
//                Thread.sleep(2000);
//                //Log.e("com.example.lendle.esopserver", "adbd");
//                //ProcessUtil.execute("su -c adbd", false);
//                //Thread.sleep(2000);
//                //ProcessUtil.execute("su -c netstat -alp", true);
//                //Thread.sleep(500);
//                Log.e("com.example.lendle.esopserver", "connect");
//                ProcessUtil.execute("su -c adb connect localhost:5555", false);
//                Thread.sleep(500);
//            } catch (Exception e) {
//                Log.e("com.example.lendle.esopserver", e.getMessage());
//            }

            this.startServers();
            this.checkNetworkStatusThread =new CheckNetworkStatusThread(context);
            this.checkNetworkStatusThread.start();

            DebugUtils.log("checkNetworkStatusThread started...", false);

            this.checkDiskspaceStatusThread=new CheckDiskspaceStatusThread(context);
            this.checkDiskspaceStatusThread.start();

            this.keepAliveThread=new KeepAliveThread(context);
            this.keepAliveThread.start();
        }
    }

    private void setupCommandDispatcher(){
        this.commandDispatcher.addExecutor(new TestOpenPDFCommandExecutor(context));
        this.commandDispatcher.addExecutor(new TestFileCopyCommandExecutor(context));
        this.commandDispatcher.addExecutor(new OpenPDFCommandExecutor(context));
        this.commandDispatcher.addExecutor(new FileCopyCommandExecutor(context));
        this.commandDispatcher.addExecutor(new PDFFlipCommandExecutor(context));
        this.commandDispatcher.addExecutor(new TerminalCommandExecutor(context));
        this.commandDispatcher.addExecutor(new OpenRemoteFileCommandExecutor(context));
        this.commandDispatcher.addExecutor(new ScreenCommandExecutor(context));
        this.commandDispatcher.addExecutor(new SetServerIPCommandExecutor());
        this.commandDispatcher.addExecutor(new RestartPackageCommandExecutor(context));
        this.commandDispatcher.addExecutor(new TestCommandExecutor(context));
        this.commandDispatcher.addExecutor(new BackHomeCommandExecutor(context));
        this.commandDispatcher.addExecutor(new ShowMessageCommandExecutor(context));
        this.commandDispatcher.addExecutor(new SetStatusCommandExecutor(context));
    }

    public void stop() throws Exception {
        this.stopServers();
        this.serverThread.interrupt();
        serverThread = null;

        if(checkNetworkStatusThread !=null){
            this.checkNetworkStatusThread.shutdown();
        }
        if(checkDiskspaceStatusThread !=null){
            this.checkDiskspaceStatusThread.shutdown();
        }
        if(this.keepAliveThread!=null){
            this.keepAliveThread.shutdown();
        }
    }

    public void startServers() throws IOException {
        GlobalContext.resetSyncTick();
        DebugUtils.log("command server started...", false);
        //cancel hello thread to prevent flooding
        helloThread=new HelloThread(context);
        helloThread.start();
        DebugUtils.log("hello server started...", false);

        queryClientsBroadCastReceiverThread=new QueryClientsBroadCastReceiverThread(context);
        queryClientsBroadCastReceiverThread.start();
        deleteFilesBroadCastReceiverThread=new DeleteFilesBroadCastReceiverThread(context);
        deleteFilesBroadCastReceiverThread.start();
        DebugUtils.log("queryClientsBroadCastReceiver server started...", false);
        running=true;
        serverSocket = new ServerSocket(Constants.SERVER_PORT, 10);
        serverThread = new ServerThread();
        serverThread.start();
    }

    public void stopServers() throws IOException, InterruptedException {
        running=false;
        if(this.serverThread!=null) {
            this.serverThread.interrupt();
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            Log.e("com.example.lendle.esopserver", "serverSocket closed");
            serverSocket = null;
        }
        if(helloThread!=null){
            helloThread.shutdown();
            helloThread.interrupt();
            helloThread=null;
        }

        if(queryClientsBroadCastReceiverThread!=null){
            queryClientsBroadCastReceiverThread.shutdown();
        }

        if(deleteFilesBroadCastReceiverThread!=null){
            deleteFilesBroadCastReceiverThread.shutdown();
        }
        Thread.sleep(500);
    }

    public synchronized boolean isServerAllOn(){
        if(!this.running){
            return false;
        }
        if(this.serverSocket==null || this.serverSocket.isClosed() || !this.serverSocket.isBound()){
            return false;
        }
        if(GlobalContext.helloFailed){
            return false;
        }
        return true;
    }

    class ServerThread extends Thread {
        public void run() {
            while (running) try {
                //DebugUtils.log("receiving");
                final Socket client = serverSocket.accept();
                new Thread(){
                    public void run(){
                        try {
                            String commandString=IOUtils.toString(client.getInputStream(), "utf-8");
                            DebugUtils.log("commandString: "+commandString);
                            Command command= CommandParser.toCommand(commandString);
                            Server.this.commandDispatcher.executeCommand(command);
                        } catch (Exception e) {
                            e.printStackTrace();
                            DebugUtils.log(""+e+": "+e.getMessage());
                        }
                    }
                }.start();
//                String commandString=IOUtils.toString(client.getInputStream(), "utf-8");
//                DebugUtils.log("commandString: "+commandString);
//                Command command= CommandParser.toCommand(commandString);
//                Server.this.commandDispatcher.executeCommand(command);
                //context.getWindow().getDecorView().getWidth();
            } catch (Exception e) {
                e.printStackTrace();
                DebugUtils.log(e+" "+e.getMessage());
            }

        }
    }
}
