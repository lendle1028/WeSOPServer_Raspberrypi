package rocks.imsofa.wesop.server.commands;

import com.example.lendle.esopserver.commands.*;
import com.example.lendle.esopserver.util.PathUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by lendle on 2014/11/24.
 */
public class TestOpenPDFCommandExecutor implements CommandExecutor{
    private Context context=null;

    public TestOpenPDFCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("test") && command.getName().equals("openPdf");
    }

    @Override
    public Object execute(Command command) throws Exception {
        File file = new File(PathUtil.getSOPFileFolder().getAbsolutePath()+"/test.pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
        Log.e("com.example.lendle.esopserver", "ready to test event");
        Thread.sleep(5000);

        int width=((Activity)context).getWindow().getDecorView().getWidth();
        int height=((Activity)context).getWindow().getDecorView().getRootView().getHeight();
        ProcessBuilder builder=new ProcessBuilder("adb", "shell", "input", "tap", ""+(width-10), ""+(height/2) );
        Process process=builder.start();
        BufferedReader input=new BufferedReader(new InputStreamReader(process.getInputStream()));
        String str=input.readLine();
        while(str!=null){
            Log.e("com.example.lendle.esopserver",str);
            str=input.readLine();
        }
        Log.e("com.example.lendle.esopserver","Decor="+((Activity)context).getWindow().getDecorView().getHeight());
        return null;
    }
}
