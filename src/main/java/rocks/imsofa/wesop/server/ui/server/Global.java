/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.stage.Stage;
import javax.servlet.ServletContext;

/**
 *
 * @author lendle
 */
public class Global {
    private static long syncTick=System.currentTimeMillis();
    public static ServletContext servletContext = null;
//    public static Clients clients = null;
//    public static Map<File, FileChunks> fileChunksTable = new HashMap<File, FileChunks>();
//    public static Dispatcher dispatcher = null;
//    public static SchedulerDaemon schedulerDaemon = null;
//    public static ProjectManager projectManager = null;
    public static TaskDetailInstanceQueue taskDetailInstanceQueue = null;
    //public static List<Map<String, String>> staticClients = new ArrayList<>();
//    public static SystemProperties systemProperties = new SystemProperties();
    public static DownloadingSessionManager downloadingSessionManager = null;

    public static Stage javafxStage = null;

    public static Object lock = new Object();
    
    public static Map session=new HashMap();
    
    public synchronized static long getNextSyncTick(long baseSyncTick){
        if(baseSyncTick==-1){
            syncTick++;
        }else{
            if(baseSyncTick>Global.syncTick){
                syncTick=baseSyncTick+1;
            }else{
                syncTick++;
            }
        }
        return syncTick;
    }
    
    public synchronized static long getSyncTick(){
        return syncTick;
    }
}
