package com.example.lendle.esopserver;

/**
 * Created by lendle on 2014/11/24.
 */
public class Constants {
    /**
     * the port used by Server
     */
    public static final int SERVER_PORT=10001;
    public static final boolean SHOW_DEBUG_TOAST=true;
    public static final boolean LOG_2_SERVER=true;
    public static final String MULTICAST_GROUP="224.0.0.100";
    /**
     * the separator between each part of an encoded file name
     */
    public static final String FILE_NAME_PART_SEPARATOR="____";
    public static final int DOWNLOAD_RETRY_COUNT=5;

    public static final String STATUS_IDLE="IDLE";
    public static final String STATUS_DOWNLOADING="DOWNLOADING";
    public static final String STATUS_DOWNLOADED="DOWNLOADED";
    public static final String STATUS_PLAYING="PLAYING";
    public static final String STATUS_TERMINATING="TERMINATING";
    public static final String STATUS_TERMINATED="TERMINATED";
    public static final String STATUS_FAILED="FAILED";
}
