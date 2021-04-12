package rocks.imsofa.wesop.server.ui;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.commons.io.IOUtils;
import rocks.imsofa.wesop.server.Constants;
import rocks.imsofa.wesop.server.commands.Command;
import rocks.imsofa.wesop.server.commands.CommandParser;
import rocks.imsofa.wesop.server.ui.server.ClientStatusMonitoringThread;
import rocks.imsofa.wesop.server.ui.server.Global;
import rocks.imsofa.wesop.server.ui.server.OpenRemoteFileAction;

public class TesterController {

    @FXML
    private TextField textBase64;

    @FXML
    private TextField textFileName;

    @FXML
    private TextField textTerminalCommand;

    @FXML
    private TextField textServerIP;

    @FXML
    private TextField textKeepAliveURL;

    @FXML
    private TextField textMessageTitle;

    @FXML
    private TextField textMessage;

    @FXML
    private TextField textStatus;

    @FXML
    private TextField textSyncTick;

    @FXML
    private TextField textFilePath;

    @FXML
    private VBox wisStatusContainer;

    @FXML
    private ComboBox<String> comboboxClients;

    private ObservableList<String> clientIps = FXCollections.observableArrayList();
    
    @FXML
    private ToggleButton buttonPressureTestOpenRemote;
    
    private OpenRemotePressureTestThread openRemotePressureTestThread=null;
    
    @FXML
    void onPressureTestOpenRemote(ActionEvent event) {
        if(openRemotePressureTestThread!=null){
            //stop previous pressure test
            openRemotePressureTestThread.shutdown();
            openRemotePressureTestThread=null;
        }
        if(buttonPressureTestOpenRemote.isSelected()){
            //do pressure test
            String clientIp = this.comboboxClients.getSelectionModel().getSelectedItem();
            if (clientIp != null) {
                openRemotePressureTestThread=new OpenRemotePressureTestThread(clientIp, textFilePath.getText(), 5000);
                openRemotePressureTestThread.start();
            }
        }
    }
    
    @FXML
    void onFileChooserButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("IMAGE Files", "*.png","*.jpg","*.jepg"),
                new FileChooser.ExtensionFilter("GIF Files", "*.gif"),
                new FileChooser.ExtensionFilter("VIDEO Files", "*.mp4","*.wmv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(textFilePath.getScene().getWindow());
        if (selectedFile != null) {
            try {
                textFilePath.setText(selectedFile.getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(TesterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void onOpenRemoteFileGOButtonClicked(ActionEvent event) {
        try {
            String clientIp = this.comboboxClients.getSelectionModel().getSelectedItem();
            if (clientIp != null) {
                OpenRemoteFileAction openRemoteFileAction = new OpenRemoteFileAction();
                openRemoteFileAction.execute(Global.servletContext, clientIp, Constants.SERVER_PORT, textFilePath.getText(), 1, false);
            }
        } catch (Exception ex) {
            Logger.getLogger(TesterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void onSetStatusGOButtonClicked(ActionEvent event) {
        final Command command = new Command();
        command.setGroupName("com.example.lendle.esopserver.commands");
        command.setName("showMessage");
        Map params = new HashMap<String, Object>();
        params.put("title", textMessageTitle.getText());
        params.put("message", textMessage.getText());
        command.setParams(params);
        this.sendCommand(command);
    }

    @FXML
    void onShowMessageGOButtonClicked(ActionEvent event) {
        final Command command = new Command();
        command.setGroupName("com.example.lendle.esopserver.commands");
        command.setName("showMessage");
        Map params = new HashMap<String, Object>();
        params.put("title", textMessageTitle.getText());
        params.put("message", textMessage.getText());
        command.setParams(params);
        this.sendCommand(command);
    }

    @FXML
    void onBackHomeGOButtonClicked(ActionEvent event) {
        final Command command = new Command();
        command.setGroupName("com.example.lendle.esopserver.commands");
        command.setName("home");
        this.sendCommand(command);
    }

    @FXML
    void onSetServerIPGOButtonClicked(ActionEvent event) {
        final Command command = new Command();
        command.setGroupName("com.example.lendle.esopserver.commands");
        command.setName("setServerIP");
        Map params = new HashMap<String, Object>();
        params.put("ip", textServerIP.getText());
        params.put("keepAliveURL", textKeepAliveURL.getText());
        command.setParams(params);
        this.sendCommand(command);
    }

    @FXML
    void onTerminalCommandGOButtonClicked(ActionEvent event) {
        final Command command = new Command();
        command.setGroupName("com.example.lendle.esopserver.commands");
        command.setName("command");
        Map params = new HashMap<String, Object>();
        params.put("command", textTerminalCommand.getText());
        command.setParams(params);
        this.sendCommand(command);
    }

    @FXML
    void onFileCopyGOButtonClicked(ActionEvent event) {
        final Command command = new Command();
        command.setGroupName("com.example.lendle.esopserver.commands");
        command.setName("copyFile");
        Map params = new HashMap<String, Object>();
        params.put("file", textBase64.getText());
        params.put("fileName", textFileName.getText());
        command.setParams(params);
        this.sendCommand(command);
    }

    @FXML
    void onTestGOButtonClicked(ActionEvent event) {
        final Command command = new Command();
        command.setGroupName("com.example.lendle.esopserver.commands");
        command.setName("testCommand");
        this.sendCommand(command);
    }

    protected void sendCommand(Command command) {
        try {
            if (this.comboboxClients.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            String clientIp = this.comboboxClients.getSelectionModel().getSelectedItem();
            if (command.getParams() == null) {
                command.setParams(new HashMap<String, Object>());
            }
            command.getParams().put("syncTick", "" + System.currentTimeMillis());
            Socket socket = new Socket(clientIp, Constants.SERVER_PORT);
            socket.setSoTimeout(10000);
            IOUtils.write(CommandParser.fromCommand(command), socket.getOutputStream(), "utf-8");
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(TesterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void initialize() {
        comboboxClients.setItems(clientIps);
        ClientStatusMonitoringThread clientStatusMonitoringThread = new ClientStatusMonitoringThread(clientIps, wisStatusContainer);
        clientStatusMonitoringThread.start();
    }
}
