package com.example.lendle.esopserver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.lendle.esopserver.activities.DummyActivity;
import com.example.lendle.esopserver.commands.Command;
import com.example.lendle.esopserver.commands.CommandParser;
import com.example.lendle.esopserver.services.ServerService;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;


public class MyActivity extends Activity {
    private Server server=null;
    private ProgressDialog progress=null;
    private static Activity mainActivity=null;

    public static Activity getMainActivity(){
        return mainActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainActivity=this;
        super.onCreate(savedInstanceState);
        server=new Server(this);
        GlobalContext.server=server;
        setContentView(R.layout.activity_my);
        //Button startButton= (Button) this.findViewById(R.id.startButton);
        progress = new ProgressDialog(this);


        Button startServiceButton= (Button) this.findViewById(R.id.startServiceButton);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        try {
                            GlobalContext.resetSyncTick();
                            Intent intent = new Intent(MyActivity.this, ServerService.class);
                            MyActivity.this.startService(intent);

                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                        } catch (Exception e) {
                            e.printStackTrace();
                            DebugUtils.log(e.getMessage());
                        }
                    }
                }.start();
            }
        });

        /*Button stopButton= (Button) this.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    server.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

        Button stopServiceButton= (Button) this.findViewById(R.id.stopServiceButton);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        try {
                            Intent intent = new Intent(MyActivity.this, ServerService.class);
                            MyActivity.this.stopService(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            DebugUtils.log(e.getMessage());
                        }
                    }
                }.start();
            }
        });

        Button screenOffButton= (Button) this.findViewById(R.id.screenOffButton);
        final ContentResolver contentResolver=getContentResolver();
        screenOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//                PowerManager.WakeLock wl = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Your Tag");
//                wl.acquire();
//                wl.release();

                setBrightness(0f);
            }
        });

        /*Button openRemoteButton= (Button) this.findViewById(R.id.remoteButton);
        openRemoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Thread(){
                        public void run(){
                            try {
                                Log.d("com.example.lendle.esopserver", InetAddress.getLocalHost().getCanonicalHostName());
                                Socket socket=new Socket(InetAddress.getLocalHost(), 10001);
                                Command command=new Command();
                                command.setGroupName("com.example.lendle.esopserver.commands");
                                command.setName("openRemotePdf");
                                IOUtils.write(CommandParser.fromCommand(command), socket.getOutputStream(), "utf-8");
                                socket.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("com.example.lendle.esopserver", e.getLocalizedMessage());
                            }
                        }
                    }.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button testButton= (Button) this.findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Thread(){
                        public void run(){
                            try {
                                Log.d("com.example.lendle.esopserver", InetAddress.getLocalHost().getCanonicalHostName());
                                Socket socket=new Socket(InetAddress.getLocalHost(), 10001);
                                Command command=new Command();
                                command.setGroupName("test");
                                command.setName("openPdf");
                                IOUtils.write(CommandParser.fromCommand(command), socket.getOutputStream(), "utf-8");
                                socket.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("com.example.lendle.esopserver", e.getLocalizedMessage());
                            }
                        }
                    }.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button copyButton= (Button) this.findViewById(R.id.copyButton);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        try {
                            DebugUtils.showToast(MyActivity.this, "copy");
                            Socket socket = new Socket(InetAddress.getLocalHost(), 10001);
                            Command command = new Command();
                            command.setGroupName("test");
                            command.setName("copyFile");
                            File dataDir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download");
                            File targetFile=new File(dataDir, "sample.pdf");
                            ByteArrayOutputStream byteBuffer=new ByteArrayOutputStream();
                            FileInputStream input=new FileInputStream(targetFile);
                            DebugUtils.showToast(MyActivity.this, "start");
                            IOUtils.copyLarge(input, byteBuffer);

                            String base64String= Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
                            //DebugUtils.showToast(MyActivity.this, base64String);
                            //Log.e("com.example.lendle.esopserver", base64String);
                            command.getParams().put("file", base64String);
                            command.getParams().put("fileName", "sample2.pdf");
                            input.close();
                            byteBuffer.close();
                            IOUtils.write(CommandParser.fromCommand(command), socket.getOutputStream(), "utf-8");
                            socket.close();
                            DebugUtils.showToast(MyActivity.this, "finished");
                        }catch (Exception e) {
                            //DebugUtils.showToast(MyActivity.this, e.getMessage());
                            //e.printStackTrace();
                            DebugUtils.log("error:"+e+" "+e.getMessage());
                        }
                    }
                }.start();
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setBrightness(float brightness){
        int brightnessInt = (int)(brightness*255);

        Settings.System.putInt(this.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightnessInt);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness;
        getWindow().setAttributes(lp);
        Intent intent = new Intent(getApplicationContext(), DummyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("brightness value", brightness);
        getApplication().startActivity(intent);
    }
}
