package rocks.imsofa.wesop.server.ui;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.commons.io.IOUtils;
import rocks.imsofa.wesop.server.Constants;
import rocks.imsofa.wesop.server.commands.Command;
import rocks.imsofa.wesop.server.commands.CommandParser;

public class TesterController {

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
