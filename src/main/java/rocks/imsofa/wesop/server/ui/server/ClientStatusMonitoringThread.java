/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rocks.imsofa.wesop.server.ui.ClientModel;

/**
 *
 * @author lendle
 */
public class ClientStatusMonitoringThread extends AbstractBroadcastMonitoringThread {

    private ObservableList<String> clientIps = null;
    private Map<String, ClientModel> clients = new HashMap<>();
    private VBox wisStatusContainer = null;

    public ClientStatusMonitoringThread(ObservableList<String> clientIps, VBox wisStatusContainer) {
        super(5000);
        this.clientIps = clientIps;
        this.wisStatusContainer = wisStatusContainer;
    }

    @Override
    public void shutdown() {
    }

    @Override
    protected void processMessage(String received) {
        Map<String, String> map = new Gson().fromJson(received, Map.class);

        String id = map.get("id");
        String ip = map.get("ip");
        int port = Integer.valueOf((map.containsKey("port")) ? map.get("port") : "10001");
//        String syncTickString = map.get("syncTick");

        if (clientIps.contains(ip) == false) {
            clientIps.add(ip);
            //create a new client model
            ClientModel clientModel = new ClientModel();
            clientModel.setId(new SimpleStringProperty(id));
            clientModel.setIp(new SimpleStringProperty(ip));
            clientModel.setLastSeen(new SimpleStringProperty("-1"));
            clientModel.setStatus(new SimpleStringProperty(""));
            clients.put(id, clientModel);

            Label idLabel = new Label();
            idLabel.textProperty().bind(clientModel.getId());
            Label ipLabel = new Label();
            ipLabel.textProperty().bind(clientModel.getIp());
            Label statusLabel = new Label();
            statusLabel.textProperty().bind(clientModel.getStatus());
            Label lastSeenLabel = new Label();
            lastSeenLabel.textProperty().bind(clientModel.getLastSeen());

            HBox hbox = new HBox(statusLabel, idLabel, ipLabel, lastSeenLabel);
            hbox.setSpacing(20);
            hbox.setPrefWidth(500);
            statusLabel.setPrefWidth(100);
            Platform.runLater(new Runnable() {
                public void run() {
                    wisStatusContainer.getChildren().add(hbox);
                }
            });
            
        } else {
            //update
            ClientModel clientModel = clients.get(id);
            Platform.runLater(new Runnable() {
                public void run() {
                    ((SimpleStringProperty) clientModel.getStatus()).setValue(map.get("status"));
                    ((SimpleStringProperty) clientModel.getLastSeen()).setValue("" + System.currentTimeMillis());
                }
            });
        }
    }

    @Override
    public long getDefaultWaiting() {
        return 1000;
    }

}
