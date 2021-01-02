package rocks.imsofa.wesop.server.commands;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import rocks.imsofa.wesop.server.GlobalContext;

/**
 * Created by lendle on 2014/11/24.
 */
public class SetStatusCommandExecutor extends AbstractCommandExecutor{
    public SetStatusCommandExecutor() {
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("setStatus");
    }

    @Override
    public synchronized Object _execute(Command command) throws Exception {
        try {
            String status= (String) command.getParams().get("status");
            GlobalContext.status=status;
            GlobalContext.advanceSyncTick(Long.valueOf(""+command.getParams().get("syncTick")));
        }catch(Exception e){throw e;}
        return null;
    }
}
