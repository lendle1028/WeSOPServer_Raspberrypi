/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import rocks.imsofa.wesop.server.DebugUtils;
import rocks.imsofa.wesop.server.GlobalContext;
import rocks.imsofa.wesop.server.Server;

/**
 * FXML Controller class
 *
 * @author lendle
 */
public class MainController implements DebugUtils.UILogger{

    @FXML
    private Label labelServerStatus;

    @FXML
    private Button buttonShowServerStatus;

    @FXML
    private ToggleButton toggleServer;
    
    @FXML
    private TextArea textLog;

    public MainController(){
        GlobalContext.uILogger=this;
    }
    
    @FXML
    void onToggleServer(MouseEvent event) {
        try{
        if (toggleServer.isSelected()) {

            GlobalContext.server = new Server();
            GlobalContext.server.start();
            buttonShowServerStatus.setStyle("-fx-background-color : green;");
            labelServerStatus.setText("Server is started");

        } else {
            GlobalContext.server.stop();
            GlobalContext.server = null;
            buttonShowServerStatus.setStyle("-fx-background-color : red;");
            labelServerStatus.setText("Server is stopped");
        }
        }catch(Exception e){
            DebugUtils.log(e, false);
        }
    }

    @Override
    public void log(String logMessage) {
        this.textLog.appendText(logMessage+"\r\n");
        this.textLog.setScrollTop(Double.MAX_VALUE);
    }

}
