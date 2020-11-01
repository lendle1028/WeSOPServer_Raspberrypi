package com.example.lendle.esopserver;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lendle on 2014/12/29.
 */
public class ProcessUtil {
    public static Process execute(String command) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(command.split(" "));
        //builder.directory(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/"));
        return builder.start();
    }

    public static Process execute(String command, boolean log) throws IOException {
        if(!log){
            return execute(command);
        }else{
            Process p=execute(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = input.readLine();
            while (str != null) {
                Log.e("com.example.lendle.esopserver", str);
                str = input.readLine();
            }
            input.close();
            return p;
        }
    }
}
