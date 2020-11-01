package com.example.lendle.esopserver.commands;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.util.PathUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by lendle on 2014/11/24.
 */
public class FileCopyCommandExecutor implements CommandExecutor{
    private Context context=null;

    public FileCopyCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("copyFile");
    }

    @Override
    public Object execute(Command command) throws Exception {
        String base64= (String) command.getParams().get("file");
        String fileName= (String) command.getParams().get("fileName");
        String fileDirectory= (String) command.getParams().get("fileDirectory");
        if(fileDirectory==null){
            fileDirectory= PathUtil.getSOPFileFolder().getAbsolutePath()+"/";
        }
        byte [] base64Bytes= Base64.decode(base64, Base64.DEFAULT);
        File dataDir=new File(fileDirectory);
        if(dataDir.exists()==false){
            dataDir.mkdirs();
        }
        File targetFile=new File(dataDir, fileName);
        FileOutputStream output=new FileOutputStream(targetFile);
        output.write(base64Bytes);
        output.close();
        Log.e("com.example.lendle.esopserver", "file copied: "+targetFile.getAbsolutePath());
        return null;
    }
}
