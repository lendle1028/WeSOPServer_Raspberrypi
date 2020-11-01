package rocks.imsofa.wesop.server.commands;

import com.example.lendle.esopserver.commands.*;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.activities.ScreenActivity;

/**
 * turn screen on and off
 * Created by lendle on 2015/1/25.
 */
public class ScreenCommandExecutor implements CommandExecutor{
    private Context context=null;

    public ScreenCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName() + ":" + command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("screen");
    }

    @Override
    public Object execute(Command command) throws Exception {
        String mode= (String) command.getParams().get("mode");//on or off
        if("off".equals(mode)) {
            Intent intent=new Intent(context, ScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
//            PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            PowerManager.WakeLock wl = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Screen Off");
//            wl.acquire();
//            wl.release();
//            DebugUtils.log("off");
//            WindowManager.LayoutParams params = context.getWindow().getAttributes();
//            params.flags |= LayoutParams.FLAG_KEEP_SCREEN_ON;
//            params.screenBrightness = 0;
//            getWindow().setAttributes(params);
        }else{
            PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = manager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Screen On");
            wl.acquire();
        }
        return null;
    }
}
