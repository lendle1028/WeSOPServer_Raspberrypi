/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author lendle
 */
public class Task {
    private List<TaskDetail> taskDetails=null;
    private Time startTime=null;
    private Time endTime=null;
    private boolean enabled=true;
    private String groupId=null;
    private String taskId=null;
    private Project project=null;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    
    
    
    public List<TaskDetail> getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(List<TaskDetail> taskDetails) {
        this.taskDetails = taskDetails;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    
    
    /*public Map toJSONFormat(){
        Map map=new HashMap();
        map.put("enabled", this.enabled);
        map.put("endTime", this.endTime);
        map.put("groupId", this.groupId);
        map.put("project", this.project.getName());
        map.put("startTime", this.startTime);
        return map;
    }*/

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.taskDetails);
        hash = 97 * hash + Objects.hashCode(this.startTime);
        hash = 97 * hash + Objects.hashCode(this.endTime);
        hash = 97 * hash + (this.enabled ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.groupId);
        hash = 97 * hash + Objects.hashCode(this.taskId);
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
        final Task other = (Task) obj;
        if (!Objects.equals(this.taskDetails, other.taskDetails)) {
            return false;
        }
        if (!Objects.equals(this.startTime, other.startTime)) {
            return false;
        }
        if (!Objects.equals(this.endTime, other.endTime)) {
            return false;
        }
        if (this.enabled != other.enabled) {
            return false;
        }
        if (!Objects.equals(this.groupId, other.groupId)) {
            return false;
        }
        if (!Objects.equals(this.taskId, other.taskId)) {
            return false;
        }
        return true;
    }
}
