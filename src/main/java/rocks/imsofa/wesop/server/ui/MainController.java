/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import rocks.imsofa.wesop.server.DebugUtils;
import rocks.imsofa.wesop.server.GlobalContext;
import rocks.imsofa.wesop.server.Server;
import rocks.imsofa.wesop.server.ui.server.ClientStatusMonitoringThread;

/**
 * FXML Controller class
 *
 * @author lendle
 */
public class MainController implements DebugUtils.UILogger {

    @FXML
    private Label labelServerStatus;

    @FXML
    private Button buttonShowServerStatus;

    @FXML
    private ToggleButton toggleServer;

    @FXML
    private TextArea textLog;

    public MainController() {
        GlobalContext.uILogger = this;
    }

    @FXML
    void onToggleServer(MouseEvent event) {
        try {
            if (toggleServer.isSelected()) {
                startServer();
            } else {
                stopServer();
            }
        } catch (Exception e) {
            DebugUtils.log(e, false);
        }
    }

    private void startServer() throws IOException {
        GlobalContext.server = new Server();
        GlobalContext.server.start();
        buttonShowServerStatus.setStyle("-fx-background-color : green;");
        labelServerStatus.setText("Server is started");
    }

    private void stopServer() throws Exception {
        GlobalContext.server.stop();
        GlobalContext.server = null;
        buttonShowServerStatus.setStyle("-fx-background-color : red;");
        labelServerStatus.setText("Server is stopped");
    }

    @Override
    public void log(String logMessage) {
        this.textLog.appendText(logMessage + "\r\n");
        this.textLog.setScrollTop(Double.MAX_VALUE);
    }

    @FXML
    public void initialize() {
        try {
            startServer();
            toggleServer.setSelected(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
