package rocks.imsofa.wesop.server.tasks.downloadfile;

import com.example.lendle.esopserver.tasks.downloadfile.*;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.lendle.esopserver.Constants;
import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.GlobalContext;
import com.example.lendle.esopserver.activities.LoadingActivity;
import com.example.lendle.esopserver.util.CRCUtil;
import com.example.lendle.esopserver.util.PathUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lendle on 2015/1/2.
 */
public class DownloadFileTask extends AsyncTask<DownloadFileArg, Void, Void> {
    private DownloadFileArg param=null;
    private File finalFile=null;
    private boolean alwaysDownload=false;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            GlobalContext.status=Constants.STATUS_DOWNLOADED;

            //notify producer
            if(param.isValid()) {
                param.getCallback().process(param, finalFile);
                GlobalContext.status=Constants.STATUS_PLAYING;
                try {
                    InputStream finishInput = param.getPlayStartReportURL().openStream();
                    IOUtils.readLines(finishInput);
                    finishInput.close();
                } catch (Exception e) {
                }
            }else{
                GlobalContext.status=Constants.STATUS_TERMINATED;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPostExecute(aVoid);

    }

    @Override
    protected Void doInBackground(DownloadFileArg... params) {
        GlobalContext.status=Constants.STATUS_DOWNLOADING;
        param=params[0];
        File sopFolder=PathUtil.getSOPFileFolder();
        if(sopFolder.exists()==false){
            sopFolder.mkdirs();
        }
        if(sopFolder.listFiles()!=null) {
            for (File file : sopFolder.listFiles()) {
                String[] nameParts = file.getName().split(Constants.FILE_NAME_PART_SEPARATOR);
                if (nameParts.length >= 3) {
                    String lastModifiedPart = nameParts[1];
                    String fileNamePart = nameParts[2];
                    DebugUtils.log(fileNamePart + ":" + lastModifiedPart + ":" + params[0].getOriginalFileName() + ":" + params[0].getOriginalFileLastModified());
                    if (fileNamePart.equals(params[0].getOriginalFileName())) {
                        file.setLastModified(System.currentTimeMillis());
                        if (Long.valueOf(lastModifiedPart) >= params[0].getOriginalFileLastModified()) {
                            if(alwaysDownload){
                                //delete old file
                                file.delete();
                                break;
                            }
                            else {
                                //check crc
                                try {
                                    if (CRCUtil.getCRC(file) != params[0].getCrc()) {
                                        break;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    break;
                                }
                                //no download is needed
                                finalFile = file;
                                DebugUtils.log("no download is needed......");
                                //notify producer
                                try {
                                    InputStream finishInput = params[0].getFinishURL(false).openStream();
                                    IOUtils.readLines(finishInput);
                                    finishInput.close();
                                } catch (Exception e) {
                                }
                                return null;
                            }
                        }
                    }
                }
            }
        }

        if(params[0].getPage()==1) {
            Intent loading = new Intent(param.getContext(), LoadingActivity.class);
            loading.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            param.getContext().getApplicationContext().startActivity(loading);
        }
        InputStream input= null;
        OutputStream output=null;
        DebugUtils.log("download for "+GlobalContext.MACHINE_ID+" from: "+params[0].getUrl(), false);

        int part=0;
        String fileString = PathUtil.getSOPFileFolder().getAbsolutePath() + "/" + params[0].getSaveFileName();
        File file = new File(fileString);
        finalFile = file;
        try {
            output = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean downloadFailed=false;
        GlobalContext.downloadingTotalParts=params[0].getNumTotalParts();
        GlobalContext.downloadingFinishedParts=0;
        while(part<params[0].getNumTotalParts() && params[0].isValid()){
            int tryCount=0;


            while(tryCount<Constants.DOWNLOAD_RETRY_COUNT){
                try {
                    tryCount++;
                    URL url=params[0].getUrl(part);
                    DebugUtils.log("download from: "+url.toString());
                    URLConnection conn=url.openConnection();

                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    input = conn.getInputStream();
                    IOUtils.copy(input, output);
                    DebugUtils.log("sessionStatus="+conn.getHeaderField("sessionStatus"));
                    if("removed".equals(conn.getHeaderField("sessionStatus"))){
                        downloadFailed=true;
                        params[0].setValid(false);
                        break;
                    }
                    downloadFailed=false;
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    downloadFailed=true;
                    e.printStackTrace();
                    DebugUtils.log("download failed for " + GlobalContext.MACHINE_ID + ": " + e + ":" + e.getMessage());
                }finally{
                    if(input!=null){
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            part++;
            GlobalContext.downloadingFinishedParts=part;
            if(part>=params[0].getNumTotalParts()){
                //check crc
                try {
                    if(output!=null) {
                        output.close();
                    }
                    long crc= CRCUtil.getCRC(file);
                    DebugUtils.log("crc local="+crc+", remote="+params[0].getCrc());

                    if(crc==params[0].getCrc()) {
                        downloadFailed = false;
                        output=null;
                    }else{
                        //re-try
                        part=0;
                        output = new FileOutputStream(file);
                        downloadFailed=true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DebugUtils.log(e+":"+e.getMessage());
                    //error happen
                    downloadFailed=true;
                }
            }
        }
        if(output!=null) {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(downloadFailed){
            DebugUtils.log("unrecoverable download failed for " + GlobalContext.MACHINE_ID);
            param.setValid(false);
            //notify producer
            try {
                InputStream finishInput = params[0].getFinishURL(false).openStream();
                IOUtils.readLines(finishInput);
                finishInput.close();
            }catch(Exception e){}
        }else{
            //notify producer
            try {
                InputStream finishInput = params[0].getFinishURL(true).openStream();
                IOUtils.readLines(finishInput);
                finishInput.close();
            }catch(Exception e){}
        }

        if(downloadFailed || !param.isValid()){
            FileUtils.deleteQuietly(file);
        }
        /*int tryCount=0;

        while(tryCount<Constants.DOWNLOAD_RETRY_COUNT) {
            try {
                tryCount++;
                input = params[0].getUrl().openStream();
                String fileString = PathUtil.getSOPFileFolder().getAbsolutePath() + "/" + params[0].getSaveFileName();
                //DebugUtils.log("2. "+fileString, false);
                File file = new File(fileString);
                finalFile = file;
                output = new FileOutputStream(file);
                IOUtils.copy(input, output);
                output.flush();
                output.close();
                input.close();
                downloadFailed=false;
                break;
            } catch (Exception e) {
                downloadFailed=true;
                e.printStackTrace();
                DebugUtils.log("download failed for " + GlobalContext.ANDROID_ID + ": " + e + ":" + e.getMessage());
            } finally {
                if (input != null) {
                    try {
                        input.close();
                        output.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if(downloadFailed){
            DebugUtils.log("unrecoverable download failed for " + GlobalContext.ANDROID_ID);
            param.setValid(false);
        }*/

        return null;
    }


}
