package com.example.lendle.esopserver.commands;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.lendle.esopserver.activities.ShowMessageActivity;
import com.example.lendle.esopserver.util.PathUtil;

import java.io.File;

/**
 * Created by lendle on 2014/11/24.
 */
public class ShowMessageCommandExecutor implements CommandExecutor{
    private Context context=null;

    public ShowMessageCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("showMessage");
    }

    @Override
    public Object execute(Command command) throws Exception {
        String title= (String) command.getParams().get("title");
        String message= (String) command.getParams().get("message");
        Intent i = new Intent(context, ShowMessageActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("message", message);
        i.putExtra("title", title);
        context.getApplicationContext().startActivity(i);
        return null;
    }
}
