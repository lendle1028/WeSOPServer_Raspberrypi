package com.example.lendle.esopserver.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.R;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        this.setTitle("Downloading");
        ProgressDialog progress=ProgressDialog.show(this, "Downloading", "Please wait while downloading......");
        progress.show();
        final BroadcastReceiver receiver=new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                unregisterReceiver(this);
                finish();
            }
        };
        IntentFilter intentFilter = new IntentFilter(
                "com.acrotech.WeSOP.finish");
        this.registerReceiver(receiver, intentFilter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
