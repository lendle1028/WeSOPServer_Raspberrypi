package com.example.lendle.esopserver.commands;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.lendle.esopserver.util.PathUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by lendle on 2014/11/24.
 */
public class OpenPDFCommandExecutor implements CommandExecutor{
    private Context context=null;

    public OpenPDFCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("openPdf");
    }

    @Override
    public Object execute(Command command) throws Exception {
        String fileName= (String) command.getParams().get("fileName");
        String fileDirectory= (String) command.getParams().get("fileDirectory");
        if(fileDirectory==null){
            fileDirectory= PathUtil.getSOPFileFolder().getAbsolutePath()+"/";
        }
        File file = new File(new File(fileDirectory), fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);

        Log.e("com.example.lendle.esopserver","file "+file+" is opened");
        return null;
    }
}
