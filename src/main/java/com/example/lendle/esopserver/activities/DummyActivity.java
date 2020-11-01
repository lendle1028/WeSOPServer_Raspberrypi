package com.example.lendle.esopserver.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.lendle.esopserver.R;

public class DummyActivity extends Activity {

    private static final int DELAYED_MESSAGE = 1;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Your Tag");
//        wl.acquire();
//        wl.release();
//        Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if(msg.what == DELAYED_MESSAGE) {
//                    //DummyActivity.this.finish();
//                }
//                super.handleMessage(msg);
//            }
//        };
//        Message message = handler.obtainMessage(DELAYED_MESSAGE);
//        handler.sendMessageDelayed(message,1000);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == DELAYED_MESSAGE) {
                    DummyActivity.this.finish();
                }
                super.handleMessage(msg);
            }
        };

        Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Intent brightnessIntent = this.getIntent();
        float brightness = brightnessIntent.getFloatExtra("brightness value", 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness;
        getWindow().setAttributes(lp);

        Message message = handler.obtainMessage(DELAYED_MESSAGE);
        //this next line is very important, you need to finish your activity with slight delay
        handler.sendMessageDelayed(message,1000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dummy, menu);
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
