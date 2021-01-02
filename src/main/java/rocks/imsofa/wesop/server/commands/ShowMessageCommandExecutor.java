package rocks.imsofa.wesop.server.commands;

import java.io.File;

/**
 * Created by lendle on 2014/11/24.
 */
public class ShowMessageCommandExecutor extends AbstractCommandExecutor{

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
//        String title= (String) command.getParams().get("title");
//        String message= (String) command.getParams().get("message");
//        Intent i = new Intent(context, ShowMessageActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.putExtra("message", message);
//        i.putExtra("title", title);
//        context.getApplicationContext().startActivity(i);
        return null;
    }
}
