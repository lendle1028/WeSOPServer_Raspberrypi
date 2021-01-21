package rocks.imsofa.wesop.server.commands;

import java.io.File;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import rocks.imsofa.wesop.server.DebugUtils;

/**
 * Created by lendle on 2014/11/24.
 */
public class ShowMessageCommandExecutor extends AbstractCommandExecutor {

    public ShowMessageCommandExecutor() {
    }

    @Override
    public boolean canHandle(Command command) {
        //Log.e("com.example.lendle.esopserver", command.getGroupName()+":"+command.getName());
        return command.getGroupName().equals("com.example.lendle.esopserver.commands") && command.getName().equals("showMessage");
    }

    @Override
    public Object _execute(Command command) throws Exception {
        //TODO: implement a way to pop up message for end users
        String title= (String) command.getParams().get("title");
        String message= (String) command.getParams().get("message");
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                Alert alert = new Alert(AlertType.INFORMATION);
    //            alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(title);
                alert.setContentText(message);
                alert.showAndWait();
            }  
        });
        
//        Intent i = new Intent(context, ShowMessageActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.putExtra("message", message);
//        i.putExtra("title", title);
//        context.getApplicationContext().startActivity(i);
        return null;
    }
}
