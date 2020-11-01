package com.example.lendle.esopserver;


import com.example.lendle.esopserver.activities.HudActivity;
import com.example.lendle.esopserver.services.SequentialTaskThread;
import com.example.lendle.esopserver.services.ServerService;
import com.example.lendle.esopserver.tasks.downloadfile.DownloadFileArg;

import java.net.URL;

/**
 * Created by lendle on 2015/1/19.
 */
public class GlobalContext {
    private static long syncTick=0;

    public static String IP=null;
    public static String MACHINE_ID=null;
    public static String flipURL=null;
    public static SequentialTaskThread sequentialTaskThread=null;
    public static HudActivity hudActivity=null;
    public static Server server=null;
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
}
