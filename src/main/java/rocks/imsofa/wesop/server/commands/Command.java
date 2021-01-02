package rocks.imsofa.wesop.server.commands;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lendle on 2014/11/24.
 */
public class Command {
    private String groupName=null;
    private String name;
    private Map<String, Object> params=new HashMap<String, Object>();
    private String endorsedExecutorClassName=null;

    public String getEndorsedExecutorClassName() {
        return endorsedExecutorClassName;
    }

    public void setEndorsedExecutorClassName(String endorsedExecutorClassName) {
        this.endorsedExecutorClassName = endorsedExecutorClassName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public static void main(String [] args) throws Exception{
        Command command=new Command();
        command.setGroupName("system");
        command.setName("test");
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("a", "a");
        params.put("b", 123);
        command.setParams(params);
        Gson gson=new Gson();
        System.out.println(gson.toJson(command));

        Command command2=gson.fromJson(gson.toJson(command), Command.class);
        System.out.println(command2.getParams().get("b").getClass());
    }
}
