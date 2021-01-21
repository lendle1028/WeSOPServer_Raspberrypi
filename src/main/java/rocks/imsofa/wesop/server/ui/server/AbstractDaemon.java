/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

/**
 *
 * @author lendle
 */
public abstract class AbstractDaemon extends Thread{
    protected boolean running = true;
    protected long defaultWaiting=30000;//default to 30 seconds, set -1 to disable waiting
    public AbstractDaemon() {
        this.setDaemon(true);
    }

    public long getDefaultWaiting() {
        return defaultWaiting;
    }

    public void setDefaultWaiting(long defaultWaiting) {
        this.defaultWaiting = defaultWaiting;
    }
    
    
    
    public void run(){
        while(running){
            try{
                if(this.defaultWaiting!=-1){
                    Thread.sleep(defaultWaiting);
                }
                this.runInBackgroundLoop();
            }catch(Exception e){}
        }
    }
    
    protected abstract void runInBackgroundLoop() throws Exception;
    
    public void shutdown(){
        this.running=false;
        this.interrupt();
    }
}
