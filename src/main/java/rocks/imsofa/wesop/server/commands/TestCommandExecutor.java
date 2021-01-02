package rocks.imsofa.wesop.server.commands;


import java.io.File;
import rocks.imsofa.wesop.server.DebugUtils;

/**
 * for testing various commands
 * Created by lendle on 2015/7/9.
 */
public class TestCommandExecutor extends AbstractCommandExecutor {

    public TestCommandExecutor() {
    }
    @Override
    public boolean canHandle(Command command) {
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("testCommand");
    }
    //TODO: we should not need this
    @Override
    public Object _execute(Command command) throws Exception {
        DebugUtils.log(command.getName());
        return null;
    }
}
