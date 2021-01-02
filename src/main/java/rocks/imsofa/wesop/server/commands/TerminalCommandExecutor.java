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
public class TerminalCommandExecutor extends AbstractCommandExecutor{
    public TerminalCommandExecutor() {
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("command");
    }

    @Override
    public Object _execute(Command command) throws Exception {
        String exec= (String) command.getParams().get("command");
        try {
            Process process = ProcessUtil.execute(exec);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str = input.readLine();
            while (str != null) {
                DebugUtils.log(str);
                str = input.readLine();
            }
            input.close();
        }catch(Exception e){
            DebugUtils.log(e, false);
        }
        return null;
    }
}
