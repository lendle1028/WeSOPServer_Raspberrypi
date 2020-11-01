package rocks.imsofa.wesop.server.util;

import java.io.File;
import rocks.imsofa.wesop.server.GlobalContext;

/**
 * Created by lendle on 2015/2/5.
 */
public class PathUtil {
    public static File getSOPFileFolder(){
        File wesopServerHome=GlobalContext.getWeSOPServerHome();
        File file=new File(wesopServerHome, "Download");
        file=new File(file, "WeSOP");
        if(file.exists()==false || !file.isDirectory()){
            file.mkdirs();
        }
        return file;
    }
}
