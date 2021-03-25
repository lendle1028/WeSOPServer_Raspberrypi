/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import rocks.imsofa.wesop.server.DebugUtils;
import rocks.imsofa.wesop.server.GlobalContext;
import rocks.imsofa.wesop.server.Server;

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
    private int logCount = 0;

    public MainController() {
        GlobalContext.uILogger = this;
        File logDir = new File("wesop_log");
        if (!logDir.exists() || !logDir.isDirectory()) {
            logDir.mkdirs();
        }
    }

    @FXML
    void onSaveLogClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(textLog.getScene().getWindow());
        if (selectedFile != null) {
            try {
                FileUtils.write(selectedFile, textLog.getText(), "utf-8");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Log Saved");
                        alert.setContentText("Log is saved to " + selectedFile.getAbsolutePath());
                        alert.showAndWait();
                    }
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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
    public synchronized void log(String logMessage) {
        logCount++;
        if (logCount > LOG_LIMIT) {
            try {
                saveLog();
                logCount = 0;
                textLog.setText("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            this.textLog.appendText(logMessage + "\r\n");
            this.textLog.setScrollTop(Double.MAX_VALUE);
        }
    }
    private static final int LOG_LIMIT = 500;

    protected synchronized void saveLog() throws IOException {
        File logDir = new File("wesop_log");
        if (!logDir.exists() || !logDir.isDirectory()) {
            logDir.mkdirs();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        File logFile = new File(logDir, "log_" + format.format(Calendar.getInstance().getTime()) + ".log");
        FileUtils.write(logFile, textLog.getText(), "utf-8");
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
