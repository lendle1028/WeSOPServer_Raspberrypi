/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;


/**
 *
 * @author lendle
 */
public class Constants {

//    public static final String CLIENTS_MAP = Clients.class.getName();
    public static final String SERVER_IP = "SERVER_IP";
    public static final String SERVER_IPS = "SERVER_IPS";
    public static final String RUNNING_TASK_QUEUE = "RUNNING_TASK_QUEUE";
    public static final String SCHEDULERDAEMON = "SCHEDULERDAEMON";
    public static final String KEY_ACCEPTED="KEY_ACCEPTED";
    public static final int DEFAULT_FILECHUNK_SIZE = 1024 * 1024;
    public static final int AWAKE_FROM_OFFLINE_ACK_COUNTS=3;

    public static final int MAX_RESCHEDULE_COUNT = 2;
    public static final long FIRST_SCHEDULE_TIMEOUT = 20000;
    public static final long PER_MEGA_DOWNLOAD_TIMEOUT = 60 * 1000;
    public static final long FILE_PLAY_TIMEOUT = 2 * 60 * 1000;
    public static final long TERMINATING_TIMEOUT = 30 * 1000;

    public static int LOCK_SOCKET_PORT = 8001;
    public static final long CLIENT_OFFLINE_TIME=30000;
    public static final String SUPER_KEY = "ACROTECH-WESOP-20151207-000000";
    public static final String LAST_SELECTED_FILE_DIRECTORY="LAST_SELECTED_FILE_DIRECTORY";//the last selected directory via file manager
    
    public static int SERVER_PORT=8080; 
}
