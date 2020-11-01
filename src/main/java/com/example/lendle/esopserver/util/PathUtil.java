package com.example.lendle.esopserver.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by lendle on 2015/2/5.
 */
public class PathUtil {
    public static File getSOPFileFolder(){
        String path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/WeSOP";
        File file=new File(path);
        if(file.exists()==false || !file.isDirectory()){
            file.mkdirs();
        }
        return file;
    }
}
