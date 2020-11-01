package com.example.lendle.esopserver.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lendle.esopserver.DebugUtils;

public class StartupBroadcastReceiver extends BroadcastReceiver {
    public StartupBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        DebugUtils.log("startup......");
        Intent _intent = new Intent(context, ServerService.class);
        context.startService(_intent);
    }
}
