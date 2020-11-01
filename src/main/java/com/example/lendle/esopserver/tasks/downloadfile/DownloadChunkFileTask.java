package com.example.lendle.esopserver.tasks.downloadfile;

import android.os.AsyncTask;

import com.example.lendle.esopserver.DebugUtils;
import com.example.lendle.esopserver.util.PathUtil;
import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lendle on 2015/1/2.
 */
public class DownloadChunkFileTask extends AsyncTask<DownloadChunkFileArg, Integer, Void> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(DownloadChunkFileArg... params) {
        InputStream input= null;
        int index=0;
        Base64 base64=new Base64();
        BufferedOutputStream output=null;
        List<File> tempFiles=new ArrayList<File>();
        String fileString= PathUtil.getSOPFileFolder().getAbsolutePath()+"/"+params[0].getSaveFileName();
        File savedFile=new File(fileString);
        try {
            output = new BufferedOutputStream(new FileOutputStream(savedFile));
            //downloading phase, simply save downloaded content
            //to temp files
            while (true) {
                try {
                    URL url = new URL(params[0].getBaseURL() + "?file=" + URLEncoder.encode(params[0].getFileName(), "utf-8") + "&index=" + index);
                    input = url.openStream();
                    String json = IOUtils.toString(input);
                    Gson gson = new Gson();
                    Map jsonMap = gson.fromJson(json, Map.class);
                    String data = (String) jsonMap.get("data");
                    int size = Integer.valueOf("" + jsonMap.get("size"));
                    File tempFile=File.createTempFile("downloadChunkFile", "chunkFile");
                    FileUtils.write(tempFile, data, "utf-8");
                    tempFiles.add(tempFile);
                    if(index>=size-1){
                        break;
                    }
                    this.publishProgress(index);
                    index++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //file reconstruction phase
            for(File file : tempFiles){
                String data=FileUtils.readFileToString(file);
                byte [] bytes=base64.decode(data);
                output.write(bytes);
                FileUtils.deleteQuietly(file);
            }
        }catch(Exception e){}
        finally{
            if(output!=null){
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //the file is ready now
        try {
            params[0].getCallback().process(params[0], savedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*DebugUtils.log("download from: "+params[0].getUrl(), false);
        try {
            input = params[0].getUrl().openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileString= PathUtil.getSOPFileFolder().getAbsolutePath()+"/"+params[0].getSaveFileName();
        File file=new File(fileString);
        try {
            OutputStream output=new FileOutputStream(file);
            IOUtils.copy(input, output);
            output.flush();
            output.close();
            input.close();
            params[0].getCallback().process(params[0], file);
        } catch (Exception e) {
            e.printStackTrace();
            DebugUtils.log("3. "+e+":"+e.getMessage());
        }*/
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        DebugUtils.log("progress: "+values[0]);
    }
}
