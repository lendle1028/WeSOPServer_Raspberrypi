/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.io.File;

/**
 *
 * @author lendle
 */
public class DownloadingSession implements java.io.Serializable{
    private String sessionId;//id of the downloading session
    private int currentPartIndex=-1;//the index of the currently downloading part
    private File originalFile=null;
    private File partFolder=null;//the folder to keep all the downloading parts
    private int numFinishedParts=0;//how many parts are already finished
    private int numTotalParts=-1;
    private int taskDetailInstanceId=-1;
    private String playerId=null;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    
    

    public int getTaskDetailInstanceId() {
        return taskDetailInstanceId;
    }

    public void setTaskDetailInstanceId(int taskDetailInstanceId) {
        this.taskDetailInstanceId = taskDetailInstanceId;
    }

    
    public int getNumTotalParts() {
        return numTotalParts;
    }

    public void setNumTotalParts(int numTotalParts) {
        this.numTotalParts = numTotalParts;
    }
    
    

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getCurrentPartIndex() {
        return currentPartIndex;
    }

    public void setCurrentPartIndex(int currentPartIndex) {
        this.currentPartIndex = currentPartIndex;
    }

    public File getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(File originalFile) {
        this.originalFile = originalFile;
    }

    public File getPartFolder() {
        return partFolder;
    }

    public void setPartFolder(File partFolder) {
        this.partFolder = partFolder;
    }

    public int getNumFinishedParts() {
        return numFinishedParts;
    }

    public void setNumFinishedParts(int numFinishedParts) {
        this.numFinishedParts = numFinishedParts;
    }
    
    
}
