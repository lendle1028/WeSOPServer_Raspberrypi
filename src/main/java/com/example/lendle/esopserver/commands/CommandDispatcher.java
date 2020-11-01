package com.example.lendle.esopserver.commands;

import android.util.Log;

import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.GlobalContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lendle on 2014/11/24.
 */
public class CommandDispatcher {
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
            Log.e("com.example.lendle.esopserver", "fittedExecutor=" + fittedExecutor);
            if (fittedExecutor == null) {
                DebugUtils.log("no fitted executor", false);
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
            DebugUtils.log(""+e+":"+e.getMessage());
            return null;
        }
    }
}
