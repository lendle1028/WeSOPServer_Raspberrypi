package rocks.imsofa.wesop.server;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import rocks.imsofa.wesop.server.util.MulticastUtil;

/**
 * Created by lendle on 2014/11/24.
 */
public class DebugUtils {
    public static void log(String message){
        log(Level.INFO, message, Constants.LOG_2_SERVER);
    }
    
    public static void log(Level level, String message){
        log(level, message, Constants.LOG_2_SERVER);
    }
    
    public static void log(Level level, String message, boolean toServer){
        Logger.getLogger(DebugUtils.class.getName()).log(level, message);
        if(GlobalContext.uILogger!=null){
            GlobalContext.uILogger.log(level+" "+new Date().toString()+":"+message);
        }
        if(toServer){
            Map<String, String> data=new HashMap<String, String>();
            data.put("host", GlobalContext.IP);
            data.put("port", "10001");
            data.put("message", message);
            data.put("level", "unknown");
            Gson gson=new Gson();

            MulticastUtil.multicast(Constants.MULTICAST_GROUP, 5002, gson.toJson(data));
        }
    }
    
    public static void log(Throwable e, boolean toServer){
        String stackTrace=Arrays.deepToString(e.getStackTrace());
        log(Level.SEVERE, stackTrace, toServer);
    }
    
    public static void log(String message, boolean b) {
        log(Level.INFO, message, b);
    }
    
    public static interface UILogger{
        public void log(String logMessage);
    }
}
