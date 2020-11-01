package com.example.lendle.esopserver.tasks.downloadfile;

import android.content.Context;

import com.example.lendle.esopserver.GlobalContext;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lendle on 2015/1/2.
 */
public class DownloadFileArg {
    private URL url=null;
    private URL finishURL=null;
    private String mimeType=null;
    private DownloadFileCallback callback=null;
    private String saveFileName=null;
    private boolean valid=true;
    private String originalFileName=null;
    private long originalFileLastModified=-1;
    private int numTotalParts=-1;
    private URL playStartReportURL=null;
    private URL terminatedReportURL=null;
    private long crc=-1;
    private Context context=null;
    private int page=-1;

    public DownloadFileArg(URL url, URL finishURL, URL playStartReportURL, URL terminatedReportURL,
                           String mimeType, String originalFileName,
                           String saveFileName, long originalFileLastModified, int numTotalParts, long crc, DownloadFileCallback callback, Context context) {
        this.mimeType = mimeType;
        this.url = url;
        this.finishURL=finishURL;
        this.playStartReportURL=playStartReportURL;
        this.terminatedReportURL=terminatedReportURL;
        this.originalFileName=originalFileName;
        this.saveFileName=saveFileName;
        this.callback = callback;
        this.originalFileLastModified=originalFileLastModified;
        this.numTotalParts=numTotalParts;
        this.crc=crc;
        this.context=context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public URL getTerminatedReportURL() {
        return terminatedReportURL;
    }

    public void setTerminatedReportURL(URL terminatedReportURL) {
        this.terminatedReportURL = terminatedReportURL;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getCrc() {
        return crc;
    }

    public void setCrc(long crc) {
        this.crc = crc;
    }

    public URL getPlayStartReportURL() {
        return playStartReportURL;
    }

    public void setPlayStartReportURL(URL playStartReportURL) {
        this.playStartReportURL = playStartReportURL;
    }

    public URL getFinishURL(boolean success) throws MalformedURLException {
        return new URL(finishURL.toString()+"&success="+success+"&playerId="+ GlobalContext.MACHINE_ID);
    }

    public void setFinishURL(URL finishURL) {
        this.finishURL = finishURL;
    }

    public int getNumTotalParts() {
        return numTotalParts;
    }

    public void setNumTotalParts(int numTotalParts) {
        this.numTotalParts = numTotalParts;
    }

    public long getOriginalFileLastModified() {
        return originalFileLastModified;
    }

    public void setOriginalFileLastModified(long originalFileLastModified) {
        this.originalFileLastModified = originalFileLastModified;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * by setting invalid, the callback will not be executed
     * @param valid
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public URL getUrl() {
        return url;
    }

    public URL getUrl(int part) throws MalformedURLException {
        return new URL(url.toString()+"&part="+part);
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public DownloadFileCallback getCallback() {
        return callback;
    }

    public void setCallback(DownloadFileCallback callback) {
        this.callback = callback;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
