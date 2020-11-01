package com.example.lendle.esopserver.commands;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.example.lendle.esopserver.ProcessUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by lendle on 2014/11/24.
 */
public class TerminalCommandExecutor implements CommandExecutor{
    private Context context=null;

    public TerminalCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("command");
    }

    @Override
    public Object execute(Command command) throws Exception {
        String exec= (String) command.getParams().get("command");
        try {
            Process process = ProcessUtil.execute(exec);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str = input.readLine();
            while (str != null) {
                Log.e("com.example.lendle.esopserver", str);
                str = input.readLine();
            }
            input.close();
        }catch(Exception e){Log.e("com.example.lendle.esopserver", e.getMessage());}
        return null;
    }
}
