package rocks.imsofa.wesop.server;

import rocks.imsofa.wesop.server.commands.*;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import rocks.imsofa.wesop.server.services.SequentialTaskThread;

/**
 * Created by lendle on 2014/10/20.111
 */
public class Server {
    private ServerSocket serverSocket = null;
    private ServerThread serverThread = null;
    private boolean running = false;
    private CommandDispatcher commandDispatcher=null;
    private HelloThread helloThread=null;
    private CheckNetworkStatusThread checkNetworkStatusThread =null;
    private QueryClientsBroadCastReceiverThread queryClientsBroadCastReceiverThread=null;
    private DeleteFilesBroadCastReceiverThread deleteFilesBroadCastReceiverThread=null;
    private CheckDiskspaceStatusThread checkDiskspaceStatusThread=null;
    private KeepAliveThread keepAliveThread=null;

    public Server() {
        this.commandDispatcher=new CommandDispatcher();
        this.setupCommandDispatcher();
        GlobalContext.server=this;
    }

    public void start() throws IOException {
        if (serverSocket == null && running == false) {
            this.startServers();
            this.checkNetworkStatusThread =new CheckNetworkStatusThread();
            this.checkNetworkStatusThread.start();

            DebugUtils.log("checkNetworkStatusThread started...", false);

            this.checkDiskspaceStatusThread=new CheckDiskspaceStatusThread();
            this.checkDiskspaceStatusThread.start();

            this.keepAliveThread=new KeepAliveThread();
            this.keepAliveThread.start();
        }
    }

    private void setupCommandDispatcher(){
        //TODO: implement command executors
        //this.commandDispatcher.addExecutor(new TestOpenPDFCommandExecutor(context));
        //this.commandDispatcher.addExecutor(new TestFileCopyCommandExecutor(context));
//        this.commandDispatcher.addExecutor(new OpenPDFCommandExecutor());
        this.commandDispatcher.addExecutor(new FileCopyCommandExecutor());
//        this.commandDispatcher.addExecutor(new PDFFlipCommandExecutor());
        this.commandDispatcher.addExecutor(new TerminalCommandExecutor());
        this.commandDispatcher.addExecutor(new OpenRemoteFileCommandExecutor());
//        this.commandDispatcher.addExecutor(new ScreenCommandExecutor());
        this.commandDispatcher.addExecutor(new SetServerIPCommandExecutor());
//        this.commandDispatcher.addExecutor(new RestartPackageCommandExecutor());
        this.commandDispatcher.addExecutor(new TestCommandExecutor());
        this.commandDispatcher.addExecutor(new BackHomeCommandExecutor());
        this.commandDispatcher.addExecutor(new ShowMessageCommandExecutor());
        this.commandDispatcher.addExecutor(new SetStatusCommandExecutor());
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
        helloThread=new HelloThread();
        helloThread.start();
        DebugUtils.log("hello server started...", false);

        queryClientsBroadCastReceiverThread=new QueryClientsBroadCastReceiverThread();
        queryClientsBroadCastReceiverThread.start();
        deleteFilesBroadCastReceiverThread=new DeleteFilesBroadCastReceiverThread();
        deleteFilesBroadCastReceiverThread.start();
        DebugUtils.log("queryClientsBroadCastReceiver server started...", false);
        running=true;
        serverSocket = new ServerSocket(Constants.SERVER_PORT, 10);
        serverThread = new ServerThread();
        serverThread.start();
        GlobalContext.sequentialTaskThread=new SequentialTaskThread();
        GlobalContext.sequentialTaskThread.start();
        GlobalContext.executorService=Executors.newCachedThreadPool();
    }

    public void stopServers() throws IOException, InterruptedException {
        running=false;
        if(this.serverThread!=null) {
            this.serverThread.interrupt();
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            DebugUtils.log("server closed");
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
        GlobalContext.sequentialTaskThread.shutdown();
        GlobalContext.executorService.shutdown();
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
        public ServerThread(){
            this.setDaemon(true);
        }
        public void run() {
            while (running) try {
                //DebugUtils.log("receiving");
                final Socket client = serverSocket.accept();
                GlobalContext.executorService.execute(new Runnable(){
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
                });
//                new Thread(){
//                    public void run(){
//                        try {
//                            String commandString=IOUtils.toString(client.getInputStream(), "utf-8");
//                            DebugUtils.log("commandString: "+commandString);
//                            Command command= CommandParser.toCommand(commandString);
//                            Server.this.commandDispatcher.executeCommand(command);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            DebugUtils.log(""+e+": "+e.getMessage());
//                        }
//                    }
//                }.start();

//                String commandString=IOUtils.toString(client.getInputStream(), "utf-8");
//                DebugUtils.log("commandString: "+commandString);
//                Command command= CommandParser.toCommand(commandString);
//                Server.this.commandDispatcher.executeCommand(command);
                //context.getWindow().getDecorView().getWidth();
            } catch (Exception e) {
                DebugUtils.log(e, false);
            }

        }
    }
}
