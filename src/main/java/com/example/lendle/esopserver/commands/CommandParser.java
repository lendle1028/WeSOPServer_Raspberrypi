package com.example.lendle.esopserver.commands;

import com.google.gson.Gson;

/**
 * Created by lendle on 2014/11/24.
 */
public class CommandParser {
    public static Command toCommand(String string){
        Gson gson=new Gson();
        return gson.fromJson(string, Command.class);
    }

    public static String fromCommand(Command command){
        Gson gson=new Gson();
        return gson.toJson(command);
    }
}
