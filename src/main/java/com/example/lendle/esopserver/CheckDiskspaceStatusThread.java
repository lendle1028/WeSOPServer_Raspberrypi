package com.example.lendle.esopserver;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;

import com.example.lendle.esopserver.util.MulticastUtil;
import com.example.lendle.esopserver.util.PathUtil;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lendle on 2015/6/22.
 */
public class CheckDiskspaceStatusThread extends Thread {
    private boolean running=true;
    private Context context=null;
    private long interval=10*60*1000;
    public CheckDiskspaceStatusThread(Context ctx){
        this.context=ctx;
    }

    public void run(){
        while(running){
            try{
                Thread.sleep(interval);
                File sopFolder= PathUtil.getSOPFileFolder();
                long bytes=0;
                File [] files=sopFolder.listFiles();
                List<File> oldFiles=new ArrayList<File>();
                if(files!=null){
                    for(File file : files){
                        bytes+=file.length();
                        String [] nameParts=file.getName().split(Constants.FILE_NAME_PART_SEPARATOR);
                        if(nameParts.length>=3){
                            //we have to do this since the current time on TVBox cannot
                            //be guaranteed
                            String lastModifiedPart=nameParts[1];
                            long lastModified=Long.valueOf(lastModifiedPart);
                            if(lastModified< (GlobalContext.lastPlayFileTime-7*24*60*60*1000)){
                                oldFiles.add(file);
                            }
                        }
                    }
                }
                if(bytes>1.5*1024*1024*1024){
                    //should delete old files
                    for(File file : oldFiles){
                        FileUtils.deleteQuietly(file);
                    }
                }
            }catch(Exception e){
                DebugUtils.log(e.toString());
            }
        }
    }

    public void shutdown(){
        this.running=false;
        this.interrupt();
    }
}
