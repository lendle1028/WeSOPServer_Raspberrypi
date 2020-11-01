package com.example.lendle.esopserver.commands;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.example.lendle.esopserver.Constants;
import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.GlobalContext;
import com.example.lendle.esopserver.ProcessUtil;
import com.example.lendle.esopserver.tasks.downloadfile.DownloadFileArg;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lendle on 2014/11/24.
 */
public class BackHomeCommandExecutor implements CommandExecutor{
    private Context context=null;
    private DownloadFileArg currentDownloadingArg=null;
    public BackHomeCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("home");
    }

    @Override
    public synchronized Object execute(Command command) throws Exception {
        try {
            currentDownloadingArg=GlobalContext.currentDownloadingArg;
            /*while(System.currentTimeMillis()<GlobalContext.delayBackUntil){
                DebugUtils.log("delayed back operation");
                Thread.sleep(1000);
            }*/
            if(currentDownloadingArg!=GlobalContext.currentDownloadingArg){
                return null;
            }
            if(GlobalContext.currentDownloadingArg!=null) {
                GlobalContext.currentDownloadingArg.setValid(false);
            }
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            DebugUtils.log("home!");
            context.getApplicationContext().startActivity(startMain);
            GlobalContext.status = Constants.STATUS_TERMINATED;
            if(GlobalContext.currentDownloadingArg!=null) {
                new Thread() {
                    public void run() {
                        //notify producer
                        try {
                            InputStream finishInput = GlobalContext.currentDownloadingArg.getTerminatedReportURL().openStream();
                            IOUtils.readLines(finishInput);
                            finishInput.close();
                            //GlobalContext.delayBackUntil=-1;
                        } catch (Exception e) {
                            DebugUtils.log("fail to report terminated status");
                        }
                    }
                }.start();
            }
        }catch(Exception e){Log.e("esopserver", e.getMessage());}
        return null;
    }
}
