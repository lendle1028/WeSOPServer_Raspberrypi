/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author lendle
 */
public class DownloadingSessionManager {
    private static int lastSessionSerial=0;
    private Map<String, DownloadingSession> downloadingStates=new HashMap<>();
    private Map<String, DownloadingSession> failedDownloadingStates=new HashMap<>();
    
    public DownloadingSession startDownloadSession(File file) throws Exception{
        String sessionId=this.getNextSessionId();
        File folder=new File(Global.servletContext.getRealPath("/WEB-INF/downloading"), sessionId);
        if(!folder.exists() || !folder.isDirectory()){
            folder.mkdirs();
        }
        List<File> files=this.createDownloadParts(folder, file);
        DownloadingSession state=new DownloadingSession();
        state.setSessionId(sessionId);
        state.setCurrentPartIndex(-1);
        state.setNumFinishedParts(0);
        state.setNumTotalParts(files.size());
        state.setOriginalFile(file);
        state.setPartFolder(folder);
        
        downloadingStates.put(sessionId, state);
        return state;
    }
    
//    public DownloadingSession startDownloadSession(File file, TaskDetailInstance instance) throws Exception{
//        String sessionId=this.getNextSessionId();
//        File folder=new File(Global.servletContext.getRealPath("/WEB-INF/downloading"), sessionId);
//        if(!folder.exists() || !folder.isDirectory()){
//            folder.mkdirs();
//        }
//        List<File> files=this.createDownloadParts(folder, file);
//        DownloadingSession state=new DownloadingSession();
//        state.setSessionId(sessionId);
//        state.setCurrentPartIndex(-1);
//        state.setNumFinishedParts(0);
//        state.setNumTotalParts(files.size());
//        state.setOriginalFile(file);
//        state.setPartFolder(folder);
//        state.setTaskDetailInstanceId(instance.getId());
//        state.setPlayerId(instance.getPlayerId());
//        downloadingStates.put(sessionId, state);
//        return state;
//    }
//    
    public File getChunkFile(String sessionId, int index){
        File folder=new File(Global.servletContext.getRealPath("/WEB-INF/downloading"), sessionId);
        return new File(folder, ""+index);
    }
    
    public DownloadingSession getDownloadSession(String sessionId){
        return this.downloadingStates.get(sessionId);
    }
    
    public List<DownloadingSession> getAllDownloadSession(){
        return new ArrayList<DownloadingSession>(this.downloadingStates.values());
    }
    
    public List<DownloadingSession> getAllFailedDownloadSession(){
        return new ArrayList<DownloadingSession>(this.failedDownloadingStates.values());
    }
    
    public void removeDownloadSession(String sessionId) throws IOException{
        File folder=new File(Global.servletContext.getRealPath("/WEB-INF/downloading"), sessionId);
        this.downloadingStates.remove(sessionId);
        FileUtils.deleteDirectory(folder);
    }
    
    public synchronized void addFailedDownloadSession(DownloadingSession session){
        this.failedDownloadingStates.put(session.getSessionId(), session);
    }
    
    public synchronized void removeFailedDownloadSession(String sessionId){
        this.failedDownloadingStates.remove(sessionId);
    }
    
    public synchronized void removeFailedDownloadSessionByPlayerId(String playerId){
        List<String> removing=new ArrayList<>();
        for(DownloadingSession session : this.failedDownloadingStates.values()){
            if(session.getPlayerId().equals(playerId)){
                removing.add(session.getSessionId());
            }
        }
        for(String id : removing){
            this.removeFailedDownloadSession(id);
        }
    }
    
    public synchronized void removeDownloadSessionByPlayerId(String playerId){
        this.removeFailedDownloadSessionByPlayerId(playerId);
        List<String> removing=new ArrayList<>();
        for(DownloadingSession session : this.downloadingStates.values()){
            if(session.getPlayerId().equals(playerId)){
                removing.add(session.getSessionId());
            }
        }
        for(String id : removing){
            try {
                this.removeDownloadSession(id);
            } catch (IOException ex) {
                Logger.getLogger(DownloadingSessionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private synchronized String getNextSessionId(){
        return ""+System.currentTimeMillis()+(lastSessionSerial++);
    }
    
    private List<File> createDownloadParts(File containerFolder, File originalFile) throws Exception{
        byte [] buffer=new byte[1024*1024];
        List<File> files=new ArrayList<>();
        try(BufferedInputStream input=new BufferedInputStream(new FileInputStream(originalFile))){
            int readBytes=input.read(buffer);
            int fileIndex=0;
            while(readBytes!=-1){
                File chunkFile=new File(containerFolder, ""+fileIndex);
                try(BufferedOutputStream output=new BufferedOutputStream(new FileOutputStream(chunkFile))){
                    output.write(buffer, 0, readBytes);
                    output.flush();
                }
                files.add(chunkFile);
                fileIndex++;
                readBytes=input.read(buffer);
            }
        }
        return files;
    }
}
