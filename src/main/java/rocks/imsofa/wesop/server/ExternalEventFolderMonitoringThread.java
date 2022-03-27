package rocks.imsofa.wesop.server;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import rocks.imsofa.wesop.server.services.ExternalEvent;
import rocks.imsofa.wesop.server.util.PathUtil;

/**
 * Created by lendle on 2015/1/8.
 */
public class ExternalEventFolderMonitoringThread extends Thread {

    private boolean running = true;
    private ExecutorService executorService = null;
    private Map<String,File> pendingFiles=new HashMap<>();

    public ExternalEventFolderMonitoringThread() {
        executorService = Executors.newFixedThreadPool(2);
    }

    public void run() {
        while (running) {
            try {
                if (!running) {
                    break;
                }
                Thread.sleep(2000);
                try {
                    final File rootFolder = PathUtil.getExternalEventsFileFolder();
                    File[] files = rootFolder.listFiles();
                    Gson gson = new Gson();
                    for (File file : files) {
                        final String key=file.getName();
                        if(pendingFiles.containsKey(key)){
                            continue;
                        }
                        if (file.getName().toLowerCase().endsWith(".json")) {
                            pendingFiles.put(key, file);
                            String json = FileUtils.readFileToString(file, "utf-8");
                            ExternalEvent event = gson.fromJson(json, ExternalEvent.class);
                            executorService.submit(new Runnable() {
                                @Override
                                public void run() {
                                    Map<String, String> data = new HashMap<String, String>();
                                    data.put("type", event.getType());
                                    data.put("eventId", event.getId());
                                    data.put("content", event.getContent());
                                    data.put("clientId", GlobalContext.MACHINE_ID);
                                    data.put("clientIp", GlobalContext.IP);
                                    System.out.println("content="+event.getContent());
                                    //int code=HttpRequest.post(GlobalContext.externalEventHandlerURL).form(data).code();
                                    int code=200;
                                    if((""+code).startsWith("2")){
                                        try {
                                            FileUtils.forceDelete(pendingFiles.get(key));
                                            pendingFiles.remove(key);
                                        } catch (IOException ex) {
                                            Logger.getLogger(ExternalEventFolderMonitoringThread.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    //DebugUtils.log("fail to issue keepAlive Packet");
                    DebugUtils.log(e, false);
                }
            } catch (Exception e) {
                DebugUtils.log(e + ":" + e.getMessage());
            }
        }
    }

    public void shutdown() {
        this.running = false;
        executorService.shutdownNow();
    }
}
