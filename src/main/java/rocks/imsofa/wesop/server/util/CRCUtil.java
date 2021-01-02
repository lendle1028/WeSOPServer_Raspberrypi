package rocks.imsofa.wesop.server.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.CRC32;
import rocks.imsofa.wesop.server.DebugUtils;

/**
 * Created by lendle on 2015/9/10.
 */
public class CRCUtil {
    public static long getCRC(File file) throws Exception{
        CRC32 crc=new CRC32();
        DebugUtils.log("calculate crc for "+file);
        InputStream input=null;
        byte [] buffer=new byte[1024];
        try{
            input=new FileInputStream(file);
            int b=input.read(buffer);
            while(b!=-1){
                crc.update(buffer, 0, b);
                b=input.read(buffer);
            }
            DebugUtils.log("crc="+crc.getValue());
            return crc.getValue();
        }finally{
            if(input!=null){
                input.close();
            }
        }
    }
}
