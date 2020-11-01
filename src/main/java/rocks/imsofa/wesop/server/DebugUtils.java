package rocks.imsofa.wesop.server;

import com.example.lendle.esopserver.util.MulticastUtil;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.rmi.runtime.Log;

/**
 * Created by lendle on 2014/11/24.
 */
public class DebugUtils {
    public static void log(Class cls, String message){
        log(cls, Level.INFO, message, Constants.LOG_2_SERVER);
    }
    
    public static void log(Class cls, Level level, String message){
        log(cls, level, message, Constants.LOG_2_SERVER);
    }
    
    public static void log(Class cls, Level level, String message, boolean toServer){
        Logger.getLogger(cls.getName()).log(level, message);
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
}
