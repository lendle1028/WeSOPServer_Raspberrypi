package com.example.lendle.esopserver.commands;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.example.lendle.esopserver.Constants;
import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.GlobalContext;
import com.example.lendle.esopserver.commands.Command;
import com.example.lendle.esopserver.commands.CommandExecutor;
import com.example.lendle.esopserver.tasks.downloadfile.DownloadFileArg;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 * Created by lendle on 2014/11/24.
 */
public class SetStatusCommandExecutor implements CommandExecutor{
    private Context context=null;
    public SetStatusCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("setStatus");
    }

    @Override
    public synchronized Object execute(Command command) throws Exception {
        try {
            String status= (String) command.getParams().get("status");
            GlobalContext.status=status;
            GlobalContext.advanceSyncTick(Long.valueOf(""+command.getParams().get("syncTick")));
        }catch(Exception e){Log.e("esopserver", e.getMessage());}
        return null;
    }
}
