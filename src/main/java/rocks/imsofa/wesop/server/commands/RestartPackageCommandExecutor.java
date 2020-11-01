package rocks.imsofa.wesop.server.commands;

import com.example.lendle.esopserver.commands.*;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.example.lendle.esopserver.activities.ScreenActivity;

/**
 * turn screen on and off
 * Created by lendle on 2015/1/25.
 */
public class RestartPackageCommandExecutor implements CommandExecutor{
    private Context context=null;

    public RestartPackageCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName() + ":" + command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("killProcess");
    }

    @Override
    public Object execute(Command command) throws Exception {
        String packageName= (String) command.getParams().get("packageName");
        ActivityManager activityManager =  (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(packageName);
        return null;
    }
}
