package rocks.imsofa.wesop.server.commands;

import java.net.URL;
import rocks.imsofa.wesop.server.DebugUtils;
import rocks.imsofa.wesop.server.GlobalContext;

/**
 * Created by lendle on 2015/6/15.
 */
public class SetServerIPCommandExecutor implements CommandExecutor {
    @Override
    public boolean canHandle(Command command) {
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("setServerIP");
    }

    @Override
    public Object execute(Command command) throws Exception {
        final String ip= (String) command.getParams().get("ip");
        final String keepAliveURL= (String) command.getParams().get("keepAliveURL");
        GlobalContext.serverIP=ip;
        if(keepAliveURL!=null){
            GlobalContext.keepAliveURL=new URL(keepAliveURL);
        }
        DebugUtils.log("server IP: "+ip);
        return ip;
    }
}
