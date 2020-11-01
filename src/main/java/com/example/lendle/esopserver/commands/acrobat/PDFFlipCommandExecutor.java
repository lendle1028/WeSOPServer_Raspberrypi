package com.example.lendle.esopserver.commands.acrobat;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.example.lendle.esopserver.commands.Command;
import com.example.lendle.esopserver.commands.CommandExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by lendle on 2014/11/24.
 */
public class PDFFlipCommandExecutor implements CommandExecutor{
    private Context context=null;

    public PDFFlipCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands.acrobat") && command.getName().equals("flip");
    }

    @Override
    public Object execute(Command command) throws Exception {
        String direction= (String) command.getParams().get("direction");//forward/backward
        String amountString=(String) command.getParams().get("amount");
        amountString=(amountString==null)?"1":amountString;
        int amount=Integer.valueOf(amountString);

        int width=((Activity)context).getWindow().getDecorView().getWidth();
        int height=((Activity)context).getWindow().getDecorView().getRootView().getHeight();

        int x=("backward".equals(direction))?10:width-10;
        int y=height/2;

        Log.e("com.example.lendle.esopserver", "x="+x+", y="+y);
        for(int i=0; i<amount; i++){
            Thread.sleep(1000);
            ProcessBuilder builder=new ProcessBuilder("su", "-c",  "adb", "-s", "localhost:5555", "shell", "input", "tap", ""+x, ""+y );
            Process process=builder.start();
            BufferedReader input=new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str=input.readLine();
            while(str!=null){
                Log.e("com.example.lendle.esopserver",str);
                str=input.readLine();
            }
            input.close();
        }
//        {
//            try {
//                ProcessBuilder builder = new ProcessBuilder("adbd");
//                Process process = builder.start();
//                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String str = input.readLine();
//                while (str != null) {
//                    Log.e("com.example.lendle.esopserver", str);
//                    str = input.readLine();
//                }
//                input.close();
//            }catch(Exception e){}
//        }
//        {
//            try {
//                ProcessBuilder builder = new ProcessBuilder("adb", "connect", "localhost");
//                Process process = builder.start();
//                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String str = input.readLine();
//                while (str != null) {
//                    Log.e("com.example.lendle.esopserver", str);
//                    str = input.readLine();
//                }
//                input.close();
//            }catch(Exception e){}
//        }
//        {
//            try {
//                ProcessBuilder builder = new ProcessBuilder("adb", "devices");
//                Process process = builder.start();
//                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String str = input.readLine();
//                while (str != null) {
//                    Log.e("com.example.lendle.esopserver", str);
//                    str = input.readLine();
//                }
//                input.close();
//            }catch(Exception e){}
//        }
        return null;
    }
}
