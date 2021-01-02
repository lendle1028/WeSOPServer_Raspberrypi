package rocks.imsofa.wesop.server.commands;


import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.util.logging.Level;
import rocks.imsofa.wesop.server.DebugUtils;
import rocks.imsofa.wesop.server.GlobalContext;
import rocks.imsofa.wesop.server.tasks.downloadfile.DownloadFileArg;

/**
 * Created by lendle on 2014/11/24.
 */
public class BackHomeCommandExecutor extends AbstractCommandExecutor{

    
    private DownloadFileArg currentDownloadingArg=null;

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("home");
    }

    @Override
    public synchronized Object _execute(Command command) throws Exception {
        try {
            currentDownloadingArg=GlobalContext.currentDownloadingArg;

            if(currentDownloadingArg!=GlobalContext.currentDownloadingArg){
                return null;
            }
            if(GlobalContext.currentDownloadingArg!=null) {
                GlobalContext.currentDownloadingArg.setValid(false);
            }
            
            if(GlobalContext.readerProcess!=null){
                GlobalContext.readerProcess.destroy();
                GlobalContext.readerProcess=null;
            }
            //TODO: implement a way to back to home screen (close and hide all opened applications)

            if(GlobalContext.currentDownloadingArg!=null) {
                new Thread() {
                    public void run() {
                        //notify producer
                        try {
                            InputStream finishInput = GlobalContext.currentDownloadingArg.getTerminatedReportURL().openStream();
                            IOUtils.readLines(finishInput);
                            finishInput.close();
                            //GlobalContext.delayBackUntil=-1;
                        } catch (Exception e) {
                            DebugUtils.log(
                                    Level.SEVERE, "fail to report terminated status");
                        }
                    }
                }.start();
            }
        }catch(Exception e){
            throw e;
        }
        return null;
    }
}
