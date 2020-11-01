package rocks.imsofa.wesop.server.commands;

import com.example.lendle.esopserver.commands.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.util.PathUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by lendle on 2014/11/24.
 */
public class TestFileCopyCommandExecutor implements CommandExecutor{
    private Context context=null;

    public TestFileCopyCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("test") && command.getName().equals("copyFile");
    }

    @Override
    public Object execute(Command command) throws Exception {
        DebugUtils.showToast(context, "execute copy");
        String base64= (String) command.getParams().get("file");
        String fileName= (String) command.getParams().get("fileName");
        byte [] base64Bytes= Base64.decode(base64, Base64.DEFAULT);
        DebugUtils.showToast(context, "received");
        //File dataDir=new File(context.getApplicationInfo().dataDir);
        File dataDir=new File(PathUtil.getSOPFileFolder().getAbsolutePath()+"/");
        File targetFile=new File(dataDir, fileName);
        DebugUtils.showToast(context, targetFile.getAbsolutePath());
        Log.e("com.example.lendle.esopserver", targetFile.getAbsolutePath());
        FileOutputStream output=new FileOutputStream(targetFile);
        output.write(base64Bytes);
        output.close();
        return null;
    }
}
