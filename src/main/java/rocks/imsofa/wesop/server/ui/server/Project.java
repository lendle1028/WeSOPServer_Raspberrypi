/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import com.google.gson.Gson;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author lendle
 */
public class Project {

    private List<Task> tasks = null;
    private boolean enabled = true;
    private String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.tasks);
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project other = (Project) obj;
        
        if(this.tasks!=null && other.tasks!=null){
            List<Task> copiedList1=new ArrayList<>(this.tasks);
            List<Task> copiedList2=new ArrayList<>(other.tasks);
            Collections.sort(copiedList1, new Comparator<Task>(){

                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getTaskId().compareTo(o2.getTaskId());
                }
            });
            
            Collections.sort(copiedList2, new Comparator<Task>(){

                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getTaskId().compareTo(o2.getTaskId());
                }
            });
            
            if(!Objects.equals(copiedList1, copiedList2)){
                return false;
            }
        }
        
        if(this.tasks!=null && other.tasks==null){
            return false;
        }
        
        if(this.tasks==null && other.tasks!=null){
            return false;
        }
        
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    

    public String toJSON() {
        //create a clone at first
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(buffer, "utf-8", false, 4);
        encoder.writeObject(this);
        encoder.close();
        XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(buffer.toByteArray()));
        Project cloned = (Project) decoder.readObject();
        decoder.close();
        //remove cycle
        for (Task task : cloned.getTasks()) {
            task.setProject(null);
            for (TaskDetail detail : task.getTaskDetails()) {
                detail.setTask(null);
            }
        }
        //use gson to generate the JSON representation
        //return new GsonBuilder().registerTypeAdapter(PlayItem.class, new PlayItemSerializer()).create().toJson(cloned);
        return GsonFactory.newGson().toJson(cloned);
    }

    public static Project fromJSON(String json) {
        Gson gson = GsonFactory.newGson();
        //Logger.getLogger(Project.class.getName()).info("json: " + json);
        Project project = gson.fromJson(json, Project.class);
        for (Task task : project.getTasks()) {
            task.setProject(project);
            for (TaskDetail detail : task.getTaskDetails()) {
                detail.setTask(task);
            }
        }
        return project;
    }

    public static List<Project> listFromJSON(String json) {
        try {
            Gson gson = GsonFactory.newGson();
            List list = gson.fromJson(json, List.class);
            List<Project> projects = new ArrayList<Project>();
            for (int i = 0; list != null && i < list.size(); i++) {
                Map map = (Map) list.get(i);
                projects.add(fromJSON(gson.toJson(map)));
            }

            return projects;
            //return GsonFactory.newGson().fromJson(json, List.class);
        } catch (Throwable e) {
            Logger.getLogger(Project.class.getName()).severe(e+":"+e.getMessage()+":"+e.getStackTrace()[0].toString());
            return null;
        }
    }
}
