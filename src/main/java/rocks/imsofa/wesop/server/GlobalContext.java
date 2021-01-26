package rocks.imsofa.wesop.server;


import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import rocks.imsofa.wesop.server.services.SequentialTaskThread;
import rocks.imsofa.wesop.server.tasks.downloadfile.DownloadFileArg;
import rocks.imsofa.wesop.server.util.IDUtil;

/**
 * Created by lendle on 2015/1/19.
 */
public class GlobalContext {
    static{
        try {
            //generate machine id
//            File wesopServerHome=getWeSOPServerHome();
//            File idFile=new File(wesopServerHome, ".id");
//            if(!idFile.exists()){
//                FileUtils.write(idFile, ""+UUID.randomUUID().toString(), "utf-8");
//            }
//            MACHINE_ID=FileUtils.readFileToString(idFile, "utf-8");
            MACHINE_ID=IDUtil.getId();
        } catch (Exception ex) {
            Logger.getLogger(GlobalContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Server server=null;
    private static long syncTick=0;

    public static String IP=null;
    public static String MACHINE_ID=null;
    public static String flipURL=null;
    public static SequentialTaskThread sequentialTaskThread=null;
//    public static HudActivity hudActivity=null;
//    public static Server server=null;
    public static String serverIP=null;
    public static URL keepAliveURL=null;
    /**
     * can be used to detect network failure
     */
    public static boolean helloFailed=false;
    public static DownloadFileArg currentDownloadingArg=null;

    public static long lastPlayFileTime=Long.MAX_VALUE;
    public static String status=Constants.STATUS_IDLE;
    public static int downloadingFinishedParts=-1;
    public static int downloadingTotalParts=-1;

    public static long delayBackUntil=-1;

    public static long pageCount=0;
    public static long flipWaitingTime=2000;//flip waiting time when pageCount reach a certain value
    
    public static Process readerProcess=null;
    public static DebugUtils.UILogger uILogger=null;
    public static ExecutorService executorService=null;
    public static Process currentOpenedFileProcess=null;

    /**
     * syncTick is used to sync async state broadcast
     * for example,
     * WIS status: IDLE(1)          Producer send play request
     * WIS status: PLAY(2)          Producer update WIS status to play
     * if (1) arrives producer later than (2), the Producer will receive wrong status
     * as a result, use syncTick to prevent old status
     * @return
     */
    public synchronized static void advanceSyncTick(long baseSyncTick){
        if(baseSyncTick==-1) {
            syncTick++;
        }else{
            syncTick=baseSyncTick+1;
        }
    }

    public synchronized static long getSyncTick(){
        return syncTick;
    }

    public synchronized static void resetSyncTick(){
        syncTick=0;
    }
    
    public static File getWeSOPServerHome(){
        File userHome=new File(System.getProperty("user.home"));
        File wesopServerHome=new File(userHome, ".wesopserver");
        if(!wesopServerHome.exists()){
            wesopServerHome.mkdirs();
        }
        return wesopServerHome;
    }
}
