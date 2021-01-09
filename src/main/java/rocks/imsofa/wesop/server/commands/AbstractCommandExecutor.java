/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.commands;

import java.util.logging.Level;
import rocks.imsofa.wesop.server.DebugUtils;

/**
 *
 * @author lendle
 */
public abstract class AbstractCommandExecutor implements CommandExecutor{

    @Override
    public Object execute(Command command) throws Exception{
        DebugUtils.log("before executing command: {"+command.getGroupName()+"}"+command.getName()+".");
        Object ret=null;
        try{
            ret=this._execute(command);
            DebugUtils.log("after command: {"+command.getGroupName()+"}"+command.getName()+" is executed.");
        }catch(Exception e){
            DebugUtils.log(e, false);
            e.printStackTrace();
            return null;
        }
        return ret;
    }
    
    protected abstract Object _execute(Command command) throws Exception;
}
