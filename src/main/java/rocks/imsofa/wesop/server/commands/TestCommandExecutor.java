package rocks.imsofa.wesop.server.commands;

import com.example.lendle.esopserver.commands.*;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.lendle.esopserver.GlobalContext;
import com.example.lendle.esopserver.tasks.downloadfile.DownloadChunkFileArg;
import com.example.lendle.esopserver.tasks.downloadfile.DownloadChunkFileCallback;
import com.example.lendle.esopserver.tasks.downloadfile.DownloadChunkFileTask;

import java.io.File;

/**
 * for testing various commands
 * Created by lendle on 2015/7/9.
 */
public class TestCommandExecutor implements CommandExecutor {
    private Context context=null;

    public TestCommandExecutor(Context context) {
        this.context = context;
    }
    @Override
    public boolean canHandle(Command command) {
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("testCommand");
    }

    @Override
    public Object execute(Command command) throws Exception {
        String commandStr= (String) command.getParams().get("command");
        if(commandStr.equals("chunkFileDownload")){
            DownloadChunkFileArg arg=new DownloadChunkFileArg("http://"+GlobalContext.serverIP+":8080/downloadChunkFile",
                    "test.pdf", "application/pdf", "test.pdf", context, new DownloadChunkFileCallback() {
                @Override
                public void process(DownloadChunkFileArg arg, File file) throws Exception {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(file), arg.getMimeType());
                    context.startActivity(intent);
                }
            });

            DownloadChunkFileTask task=new DownloadChunkFileTask();
            task.execute(arg);

        }
        return null;
    }
}
