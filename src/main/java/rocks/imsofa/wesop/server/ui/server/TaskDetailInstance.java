/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/**
 * a running TaskDetail
 * @author lendle
 */
public class TaskDetailInstance {
    private int id=-1;
    private static int lastId=100;
    private TaskDetail taskDetail=null;
    private int currentDisplayingPage=-1;
    private Date lastFlipped=null;
    private Date lastUpdated=null;
    transient private int [] availablePages=null;
    private String playerId=null;
    private String taskName=null;
    private String groupName=null;
    private String projectName=null;
    private int failCount=0;
    
    private PlayerStatus status=PlayerStatus.CREATED;
    private long currentStatusValidThrough=-1;//the time the current status will be considered timeout
    private int totalFlippedCount=0;//the total amount of flipping
    private long startTime=0;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    

    public int getTotalFlippedCount() {
        return totalFlippedCount;
    }

    public void setTotalFlippedCount(int totalFlippedCount) {
        this.totalFlippedCount = totalFlippedCount;
    }
    
    

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    
    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public long getCurrentStatusValidThrough() {
        return currentStatusValidThrough;
    }

    public void setCurrentStatusValidThrough(long currentStatusValidThrough) {
        this.currentStatusValidThrough = currentStatusValidThrough;
    }
    
    

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public static int getLastId() {
        return lastId;
    }

    public static void setLastId(int lastId) {
        TaskDetailInstance.lastId = lastId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    

    public int[] getAvailablePages() {
        return availablePages;
    }

    public void setAvailablePages(int[] availablePages) {
        this.availablePages = availablePages;
    }

    public TaskDetailInstance() {
        this.id=++lastId;
    }
    
    public TaskDetail getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(TaskDetail taskDetail) {
        this.taskDetail = taskDetail;
    }

    public int getCurrentDisplayingPage() {
        return currentDisplayingPage;
    }

    public void setCurrentDisplayingPage(int currentDisplayingPage) {
        this.currentDisplayingPage = currentDisplayingPage;
    }

    public Date getLastFlipped() {
        return lastFlipped;
    }

    public void setLastFlipped(Date lastFlipped) {
        this.lastFlipped = lastFlipped;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
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
        final TaskDetailInstance other = (TaskDetailInstance) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    public Map toJSONFormat(){
        Map map=new HashMap();
        map.put("id", this.id);
        //map.put("taskDetail", taskDetail.toJSONFormat());
        map.put("currentDisplayingPage", currentDisplayingPage);
        map.put("lastFlipped", lastFlipped.getTime());
        map.put("lastUpdated", lastUpdated.getTime());
        map.put("availablePages", availablePages);
        map.put("playerId", this.playerId);
        map.put("taskName", this.taskName);
        map.put("groupName", this.groupName);
        map.put("projectName", this.projectName);
        map.put("status", this.status);
        return map;
    }
}
