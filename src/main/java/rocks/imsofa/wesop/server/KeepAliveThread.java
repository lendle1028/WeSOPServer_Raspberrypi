package rocks.imsofa.wesop.server;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 * Created by lendle on 2015/1/8.
 */
public class KeepAliveThread extends Thread{
    private boolean running=true;

    public KeepAliveThread() {
    }

    public void run(){
        while(running){
            try {
                if(!running){
                    break;
                }
                Thread.sleep((long)(30000+10000*((Math.random()*10000)%3)));
                try {
                    InputStream finishInput = GlobalContext.keepAliveURL.openStream();
                    //TODO: readLines must be replaced
//                    IOUtils.readLines(finishInput);
                    IOUtils.readLines(finishInput, "utf-8");
                    finishInput.close();
                    //GlobalContext.delayBackUntil=-1;
                } catch (Exception e) {
                    //DebugUtils.log("fail to issue keepAlive Packet");
                    DebugUtils.log(e, false);
                }
            }catch(Exception e){
                DebugUtils.log(e+":"+e.getMessage());
            }
        }
    }

    public void shutdown(){
        this.running=false;
    }
}
