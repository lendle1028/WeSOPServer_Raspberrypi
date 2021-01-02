package rocks.imsofa.wesop.server.commands;

import java.io.File;
import rocks.imsofa.wesop.server.util.PathUtil;

/**
 * Created by lendle on 2014/11/24.
 */
public class OpenPDFCommandExecutor extends AbstractCommandExecutor{

    public OpenPDFCommandExecutor() {
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("openPdf");
    }

    @Override
    public Object _execute(Command command) throws Exception {
        String fileName= (String) command.getParams().get("fileName");
        String fileDirectory= (String) command.getParams().get("fileDirectory");
        if(fileDirectory==null){
            fileDirectory= PathUtil.getSOPFileFolder().getAbsolutePath()+"/";
        }
        File file = new File(new File(fileDirectory), fileName);
        //TODO: implement a way to open pdf file or delegate this to openRemoteFileCommandExecutor
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        context.startActivity(intent);
//
//        Log.e("com.example.lendle.esopserver","file "+file+" is opened");
        return null;
    }
}
