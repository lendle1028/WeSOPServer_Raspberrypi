package rocks.imsofa.wesop.server.ui.server;


import org.apache.commons.io.IOUtils;

import java.net.*;
import java.util.*;
import java.io.File;
import java.nio.file.Files;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import rocks.imsofa.wesop.server.commands.Command;
import rocks.imsofa.wesop.server.commands.CommandParser;

public class OpenRemoteFileAction {

    /**
     * autoflip=false will ignore page settings
     *
     * @param application
     * @param host
     * @param port
     * @param file
     * @param page
     * @param autoflip
     * @throws Exception
     */
    public void execute(ServletContext application, 
            final String host, final int port, String file, int page, boolean autoflip) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        //determine the real file to be downloaded
        File fileObject = new File(file);
        
        //final long timeout=(fileObject.length()/1024*1024)+1*Constants.PER_MEGA_DOWNLOAD_TIMEOUT;
        //Logger.getLogger(this.getClass().getName()).info(fileObject.getAbsolutePath()+":"+fileObject.exists()+":"+instance.getTaskDetail().getPages());
        if (fileObject.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
                SplitPDFFileAction action = new SplitPDFFileAction();
                fileObject = action.execute(fileObject, page);
                file = fileObject.getName();
       
        }
        ///////////////////////////////////////////
        String serverIP = "127.0.0.1";
        final Command command = new Command();
        command.setGroupName("com.example.lendle.esopserver.commands");
        command.setName("openRemoteFile");

        params.put("fileName", fileObject.getName());
        params.put("autoflip", autoflip);

        //fileObject.setLastModified(System.currentTimeMillis());
        //prepare the downloading session
//        Global.downloadingSessionManager.removeFailedDownloadSessionByPlayerId(instance.getPlayerId());
        DownloadingSession session = Global.downloadingSessionManager.startDownloadSession(fileObject);
        params.put("sessionId", session.getSessionId());
        params.put("numParts", "" + session.getNumTotalParts());
        params.put("lastModified", "" + fileObject.lastModified());
        params.put("page", page);
        String mimeType = Files.probeContentType(fileObject.toPath());
        if(mimeType==null){
            //guess mimetype if cannot probe
            String name=fileObject.getName().toLowerCase();
            if(name.endsWith("pdf")){
                mimeType="application/pdf";
            }else if(name.endsWith("mp4")){
                mimeType="video/mp4";
            }else if(name.endsWith("mp3")){
                mimeType="audio/mp3";
            }else if(name.endsWith("jpg")){
                mimeType="image/jpeg";
            }else if(name.endsWith("jpeg")){
                mimeType="image/jpeg";
            }
        }
        params.put("mimeType", mimeType);
        params.put("crc", "" + CRCUtil.getCRC(fileObject));
        Logger.getLogger(this.getClass().getName()).info("mimeType=" + mimeType);
        String serverPath = application.getContextPath();
        if (serverPath.equals("/")) {
            serverPath = "";
        }
        params.put("fileURL", "http://" + serverIP + ":"+Constants.SERVER_PORT + serverPath + "/download?file="
                + URLEncoder.encode(fileObject.getName(), "utf-8") + "&page=" + page + "&autoflip=" + autoflip + "&sessionId=" + session.getSessionId());
        params.put("finishURL", "http://" + serverIP + ":"+Constants.SERVER_PORT + serverPath + "/finishDownload?&sessionId=" + session.getSessionId());
        params.put("playStartReportURL", "http://" + serverIP + ":"+Constants.SERVER_PORT + serverPath + "/playStartReport?&instanceId=1");
        params.put("terminatedReportURL", "http://" + serverIP + ":"+Constants.SERVER_PORT + serverPath + "/terminatedReport?&instanceId=1");
        //System.out.println("http://"+serverIP+":8080"+serverPath+"/download?file="+URLEncoder.encode(file, "utf-8")+"&page="+page);
        params.put("flipURL", "http://" + serverIP + ":"+Constants.SERVER_PORT + serverPath + "/flip");
        Logger.getLogger(this.getClass().getName()).info(host + " open file http://" + serverIP + ":"+Constants.SERVER_PORT + serverPath + "/download?file=" + URLEncoder.encode(file, "utf-8") + "&page=" + page);

        command.setParams(params);
        Socket socket = new Socket(host, port);
        socket.setSoTimeout(10000);
        IOUtils.write(CommandParser.fromCommand(command), socket.getOutputStream(), "utf-8");
        socket.close();
//        instance.setCurrentStatusValidThrough(System.currentTimeMillis() + (fileObject.length() / (1024 * 1024)) * Constants.PER_MEGA_DOWNLOAD_TIMEOUT);
//        new Thread(){
//            public void run(){
//                try {
//                    Socket socket = new Socket(host, port);
//                    IOUtils.write(CommandParser.fromCommand(command), socket.getOutputStream(), "utf-8");
//                    socket.close();
//                    new SetTaskDetailInstanceStatusAction().execute(instance, PlayerStatus.DOWNLOADING);
//                } catch (IOException ex) {
//                    Logger.getLogger(OpenRemoteFileAction.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }.start();

        //JOptionPane.showMessageDialog((JFrame) null, "OK!");
    }
}
