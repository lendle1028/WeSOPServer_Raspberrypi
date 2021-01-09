package rocks.imsofa.wesop.server.ui;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.io.IOUtils;
import rocks.imsofa.wesop.server.Constants;
import rocks.imsofa.wesop.server.commands.Command;
import rocks.imsofa.wesop.server.commands.CommandParser;
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
    void onFileChooserButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
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
            OpenRemoteFileAction openRemoteFileAction = new OpenRemoteFileAction();
            openRemoteFileAction.execute(Global.servletContext, "127.0.0.1", Constants.SERVER_PORT, textFilePath.getText(), 1, false);
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
        command.setName("setStatus");
        Map params = new HashMap<String, Object>();
        params.put("status", textStatus.getText());
        params.put("syncTick", textSyncTick.getText());
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
            if (command.getParams() == null) {
                command.setParams(new HashMap<String, Object>());
            }
            command.getParams().put("syncTick", "" + System.currentTimeMillis());
            Socket socket = new Socket("localhost", Constants.SERVER_PORT);
            socket.setSoTimeout(10000);
            IOUtils.write(CommandParser.fromCommand(command), socket.getOutputStream(), "utf-8");
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(TesterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
