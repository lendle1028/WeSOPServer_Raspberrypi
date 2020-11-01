package com.example.lendle.esopserver.commands;

/**
 * Created by lendle on 2014/11/24.
 */
public interface CommandExecutor {
    /**
     * return true if the executor can handle the given command
     * @param command
     * @return
     */
    public boolean canHandle(Command command);
    public Object execute(Command command) throws Exception;
}
