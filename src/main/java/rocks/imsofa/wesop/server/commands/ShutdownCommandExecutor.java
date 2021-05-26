package rocks.imsofa.wesop.server.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import rocks.imsofa.wesop.server.DebugUtils;
import rocks.imsofa.wesop.server.ProcessUtil;

/**
 * Created by lendle on 2014/11/24.
 */
public class ShutdownCommandExecutor extends AbstractCommandExecutor{
    public ShutdownCommandExecutor() {
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("shutdown");
    }

    @Override
    public Object _execute(Command command) throws Exception {
        String password= (String) command.getParams().get("password");
        ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", "echo "+password+" | sudo -S shutdown now");
        builder.start();
        return null;
    }
}
