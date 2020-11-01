package com.example.lendle.esopserver.commands;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.lendle.esopserver.Constants;
import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.GlobalContext;
import com.example.lendle.esopserver.services.SequentialTaskThread;
import com.example.lendle.esopserver.services.TaskExecutionCallback;
import com.example.lendle.esopserver.tasks.downloadfile.DownloadFileArg;
import com.example.lendle.esopserver.tasks.downloadfile.DownloadFileCallback;
import com.example.lendle.esopserver.tasks.downloadfile.DownloadFileTask;
import com.example.lendle.esopserver.util.PathUtil;

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static com.example.lendle.esopserver.GlobalContext.sequentialTaskThread;

/**
 * Created by lendle on 2014/11/24.
 */
public class OpenRemoteFileCommandExecutor implements CommandExecutor{
    private Context context=null;

    public OpenRemoteFileCommandExecutor(Context context) {
        this.context = context;
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("openRemoteFile");
    }

    @Override
    public synchronized Object execute(Command command) throws Exception {
        GlobalContext.delayBackUntil=System.currentTimeMillis()+10000;

        final String fileURLString=(String) command.getParams().get("fileURL");
        final String finishURLString=(String) command.getParams().get("finishURL");
        final String playStartReportURLString=(String) command.getParams().get("playStartReportURL");
        final String terminatedReportURLString=(String) command.getParams().get("terminatedReportURL");
        final String mimeType=(String) command.getParams().get("mimeType");
        final String flipURL= (String) command.getParams().get("flipURL");
        final long lastModified= Long.valueOf(""+command.getParams().get("lastModified"));
        final int numTotalParts=Integer.valueOf(""+command.getParams().get("numParts"));
        final long crc=Long.valueOf(""+command.getParams().get("crc"));
        final int page=Double.valueOf(""+command.getParams().get("page")).intValue();

        GlobalContext.lastPlayFileTime=lastModified;
        GlobalContext.downloadingTotalParts=numTotalParts;


        //clean files
        try {
            File wesopFolder = PathUtil.getSOPFileFolder();
            File[] files = wesopFolder.listFiles();
            for (File file : files) {
                if(file.lastModified()<(System.currentTimeMillis()-7*24*60*60*1000)) {
                    FileUtils.deleteQuietly(file);
                }
            }
        }catch(Exception e){DebugUtils.log(""+e+":"+e.getMessage());}

        sequentialTaskThread.addTask(new SequentialTaskThread.Task(){

            @Override
            public void execute(final TaskExecutionCallback callback) {
                GlobalContext.flipURL=flipURL;
                if(GlobalContext.hudActivity!=null){
                    DebugUtils.log("reset", false);
                    GlobalContext.hudActivity.finish();
                    GlobalContext.hudActivity=null;
                }
                /*Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startMain.addCategory(Intent.CATEGORY_HOME);
                context.startActivity(startMain);*/

                try {
                    URL fileURL = new URL(fileURLString);
                    URL finishURL=new URL(finishURLString);
                    URL playStartReportURL=new URL(playStartReportURLString);
                    URL terminatedReportURL=new URL(terminatedReportURLString);
                    final SequentialTaskThread.Task self=this;
                    //String fileName= fileURL.getFile().substring(fileURL.getFile().indexOf("=")+1, fileURL.getFile().lastIndexOf("&"));
                    List<NameValuePair> params = URLEncodedUtils.parse(new URI(fileURL.toString()), "utf-8");
                    String _fileName = null, _page = null;
                    for (NameValuePair param : params) {
                        if (param.getName().equals("file")) {
                            _fileName = param.getValue();
                        }
                        if (param.getName().equals("page")) {
                            _page = param.getValue();
                        }
                    }
                    final String fileName = UUID.randomUUID().toString()+
                            Constants.FILE_NAME_PART_SEPARATOR +
                            lastModified+
                            Constants.FILE_NAME_PART_SEPARATOR+
                            _fileName;
                    String fileDirectory = PathUtil.getSOPFileFolder().getAbsolutePath() + "/";

                    //downloading the file can take sometime, so a loading message is welcomed
                    final ProgressDialog[] progress = new ProgressDialog[1];
                    /*if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress[0] = new ProgressDialog(context);
                                progress[0].setTitle("Opening");
                                progress[0].show();
                            }
                        });
                    }*/

                    DownloadFileCallback downloadFileCallback=new DownloadFileCallback() {
                        public void process(DownloadFileArg arg, final File file) throws Exception {
                            try {
                                if(arg.isValid()) {
                                    DebugUtils.log("download complete, executing......");
                                    DebugUtils.log("OpenRemoteFileCommandExecutor: " + fileName);

                                    if(GlobalContext.currentDownloadingArg!=null && GlobalContext.currentDownloadingArg.isValid() && GlobalContext.pageCount>=20) {
                                        //then there is a current displaying file, we have to close it in advance
                                        GlobalContext.pageCount=0;
                                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                                        startMain.addCategory(Intent.CATEGORY_HOME);
                                        //startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startMain.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        context.getApplicationContext().startActivity(startMain);
                                        new Thread(){
                                            public void run(){
                                                try {
                                                    Thread.sleep(GlobalContext.flipWaitingTime);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                GlobalContext.pageCount = GlobalContext.pageCount + 1;
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                intent.setDataAndType(Uri.fromFile(file), mimeType);

                                                try {
                                                    context.getApplicationContext().startActivity(intent);
                                                    DebugUtils.log("open intent sent for " + Uri.fromFile(file) + ":" + mimeType + ":" + context + ":" + sequentialTaskThread.getQueueLength());
                                                    GlobalContext.delayBackUntil = System.currentTimeMillis() + 1000;
                                                } catch (Exception e) {
                                                    //DebugUtils.showToast(context, "no supporting activities");
                                                    DebugUtils.log("OpenRemoteFileCommandExecutor fail: " + e);
                                                    callback.onFailed(self);
                                                    Intent i = new Intent("com.acrotech.WeSOP.finish");
                                                    context.sendBroadcast(i);
                                                }
                                            }
                                        }.start();

                                    }else {

                                        GlobalContext.pageCount = GlobalContext.pageCount + 1;
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        intent.setDataAndType(Uri.fromFile(file), mimeType);

                                        try {
                                            context.getApplicationContext().startActivity(intent);
                                            DebugUtils.log("open intent sent for " + Uri.fromFile(file) + ":" + mimeType + ":" + context + ":" + sequentialTaskThread.getQueueLength());
                                            GlobalContext.delayBackUntil = System.currentTimeMillis() + 1000;
                                        } catch (Exception e) {
                                            //DebugUtils.showToast(context, "no supporting activities");
                                            DebugUtils.log("OpenRemoteFileCommandExecutor fail: " + e);
                                            callback.onFailed(self);
                                            Intent i = new Intent("com.acrotech.WeSOP.finish");
                                            context.sendBroadcast(i);
                                        }
                                    }
                                }else{
                                    DebugUtils.log("download complete, but execution cancelled......");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //DebugUtils.log("open andoc", false);
                            callback.onFinished(self);
                            Intent i = new Intent("com.acrotech.WeSOP.finish");
                            context.sendBroadcast(i);
                        }
                    };
                    DownloadFileArg arg = new DownloadFileArg(fileURL, finishURL, playStartReportURL, terminatedReportURL, mimeType, _fileName, fileName, lastModified, numTotalParts, crc, downloadFileCallback, context);
                    arg.setPage(page);
                    //track the downloading task
                    if(GlobalContext.currentDownloadingArg!=null){
                        GlobalContext.currentDownloadingArg.setValid(false);
                    }
                    GlobalContext.currentDownloadingArg=arg;
                    //check if download is needed or not
                    new DownloadFileTask().execute(arg);
                }catch(Exception e){
                    callback.onFailed(this);
                    DebugUtils.log(""+e+":"+e.getMessage());
                }
            }
        });

        return null;
    }
}

