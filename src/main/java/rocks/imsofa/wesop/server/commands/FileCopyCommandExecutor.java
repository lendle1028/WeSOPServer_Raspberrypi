package rocks.imsofa.wesop.server.commands;


import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.codec.binary.Base64;
import rocks.imsofa.wesop.server.util.PathUtil;

/**
 * Created by lendle on 2014/11/24.
 */
public class FileCopyCommandExecutor extends AbstractCommandExecutor{

    public FileCopyCommandExecutor() {
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("copyFile");
    }

    @Override
    public Object _execute(Command command) throws Exception {
        String base64= (String) command.getParams().get("file");
        String fileName= (String) command.getParams().get("fileName");
        String fileDirectory= (String) command.getParams().get("fileDirectory");
        if(fileDirectory==null){
            fileDirectory= PathUtil.getSOPFileFolder().getAbsolutePath()+"/";
        }
        byte [] base64Bytes= Base64.decodeBase64(base64);
        File dataDir=new File(fileDirectory);
        if(dataDir.exists()==false){
            dataDir.mkdirs();
        }
        File targetFile=new File(dataDir, fileName);
        FileOutputStream output=new FileOutputStream(targetFile);
        output.write(base64Bytes);
        output.close();
        return null;
    }
}
