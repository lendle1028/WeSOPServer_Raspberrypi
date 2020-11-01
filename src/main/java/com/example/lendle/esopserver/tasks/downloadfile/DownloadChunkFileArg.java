package com.example.lendle.esopserver.tasks.downloadfile;

import android.content.Context;

import java.net.URL;

/**
 * Created by lendle on 2015/1/2.
 */
public class DownloadChunkFileArg {
    private String baseURL=null;
    private String mimeType=null;
    private String fileName=null;
    private DownloadChunkFileCallback callback=null;
    private String saveFileName=null;
    private Context context=null;

    public DownloadChunkFileArg(String baseURL, String fileName, String mimeType, String saveFileName, Context context, DownloadChunkFileCallback callback) {
        this.mimeType = mimeType;
        this.baseURL = baseURL;
        this.fileName=fileName;
        this.saveFileName=saveFileName;
        this.context=context;
        this.callback = callback;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DownloadChunkFileCallback getCallback() {
        return callback;
    }

    public void setCallback(DownloadChunkFileCallback callback) {
        this.callback = callback;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
