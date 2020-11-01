package rocks.imsofa.wesop.server.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import rocks.imsofa.wesop.server.GlobalContext;

/**
 * Created by lendle on 2014/11/24.
 */
public class CommandDispatcher {

    private static final Logger LOG = Logger.getLogger(CommandDispatcher.class.getName());
    
    private List<CommandExecutor> executors=new ArrayList<CommandExecutor>();

    public void addExecutor(CommandExecutor executor){
        this.executors.add(executor);
    }

    public Object executeCommand(Command command) throws Exception{
        CommandExecutor fittedExecutor=null;
        try {
            for (CommandExecutor executor : executors) {
                if (executor.getClass().getName().equals(command.getEndorsedExecutorClassName())) {
                    fittedExecutor = executor;
                    break;
                } else {
                    if (fittedExecutor == null && executor.canHandle(command)) {
                        fittedExecutor = executor;
                        if (command.getEndorsedExecutorClassName() == null) {
                            break;
                        }
                    }
                }
            }
            LOG.info("fittedExecutor=" + fittedExecutor);
            if (fittedExecutor == null) {
                LOG.severe("no fitted executor");
                throw new Exception("no fitted executor");
            }
            if(command.getParams().get("syncTick")!=null){
                long baseSyncTick=Long.valueOf("" + command.getParams().get("syncTick"));
                //DebugUtils.log("received syncTick="+baseSyncTick+", current syncTick="+GlobalContext.getSyncTick());
                GlobalContext.advanceSyncTick(baseSyncTick);
                //DebugUtils.log("received syncTick="+baseSyncTick+", current syncTick="+GlobalContext.getSyncTick());
            }
            return fittedExecutor.execute(command);
        }catch(Exception e){
            LOG.severe(e.getMessage());
            return null;
        }
    }
}
