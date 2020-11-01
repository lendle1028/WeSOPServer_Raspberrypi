package com.example.lendle.esopserver.files;

import android.content.Context;

import java.io.File;

/**
 * Created by lendle on 2015/6/22.
 */
public class FileManager {
    private Context context=null;

    public FileManager(Context context) {
        this.context = context;
    }

    public File getFolder(FolderType type){
        switch(type){
            case DOWNLOADING:
                return new File(context.getFilesDir(), "_downloading");
            case FILES:
                return new File(context.getFilesDir(), "_files");
            case TEMP:
                return new File(context.getFilesDir(), "_temp");
        };
        return null;
    }
}
