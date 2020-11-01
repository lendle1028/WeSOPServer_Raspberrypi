package com.example.lendle.esopserver.tasks.downloadfile;

import java.io.File;

/**
 * Created by lendle on 2015/1/2.
 */
public interface DownloadFileCallback {
    public void process(DownloadFileArg arg, File file) throws Exception;
}
