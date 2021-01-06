/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * the detail settings of a Task
 * @author lendle
 */
public class TaskDetail {
    private String playerId=null;
    private PlayMode playMode=null;
    private PlayItem playItem=null;
    private boolean autoFlip=false;
    private int [] pages=null;//null means all pages are included in this task
    private int autoFlipIntervalSeconds=-1;//-1 means deactivated
    private boolean autoUpdate=false;
    private int autoUpdateSeconds=-1;
    private boolean enabled=true;
    private Task task=null;
    
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }

    public PlayItem getPlayItem() {
        return playItem;
    }

    public void setPlayItem(PlayItem playItem) {
        this.playItem = playItem;
    }

    public boolean isAutoFlip() {
        return autoFlip;
    }

    public void setAutoFlip(boolean autoFlip) {
        this.autoFlip = autoFlip;
    }

    public int[] getPages() {
        return pages;
    }

    public void setPages(int[] pages) {
        this.pages = pages;
    }

    public int getAutoFlipIntervalSeconds() {
        return autoFlipIntervalSeconds;
    }

    public void setAutoFlipIntervalSeconds(int autoFlipIntervalSeconds) {
        this.autoFlipIntervalSeconds = autoFlipIntervalSeconds;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public int getAutoUpdateSeconds() {
        return autoUpdateSeconds;
    }

    public void setAutoUpdateSeconds(int autoUpdateSeconds) {
        this.autoUpdateSeconds = autoUpdateSeconds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /*public Map toJSONFormat(){
        Map map=new HashMap();
        map.put("playerId", playerId);
        map.put("autoFlip", this.autoFlip);
        map.put("autoFlipIntervalSeconds", this.autoFlipIntervalSeconds);
        map.put("autoUpdate", this.autoUpdate);
        map.put("autoUpdateSeconds", this.autoUpdateSeconds);
        map.put("enabled", this.enabled);
        map.put("pages", this.pages);
        map.put("playItem", this.playItem.getDisplayName());
        map.put("playMode", this.playMode);
        map.put("task", this.task);
        return map;
    }*/

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.playerId);
        hash = 17 * hash + Objects.hashCode(this.playMode);
        hash = 17 * hash + Objects.hashCode(this.playItem);
        hash = 17 * hash + (this.autoFlip ? 1 : 0);
        hash = 17 * hash + Arrays.hashCode(this.pages);
        hash = 17 * hash + this.autoFlipIntervalSeconds;
        hash = 17 * hash + (this.autoUpdate ? 1 : 0);
        hash = 17 * hash + this.autoUpdateSeconds;
        hash = 17 * hash + (this.enabled ? 1 : 0);
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
        final TaskDetail other = (TaskDetail) obj;
        if (!Objects.equals(this.playerId, other.playerId)) {
            return false;
        }
        if (this.playMode != other.playMode) {
            return false;
        }
        if (!Objects.equals(this.playItem, other.playItem)) {
            return false;
        }
        if (this.autoFlip != other.autoFlip) {
            return false;
        }
        if (!Arrays.equals(this.pages, other.pages)) {
            return false;
        }
        if (this.autoFlipIntervalSeconds != other.autoFlipIntervalSeconds) {
            return false;
        }
        if (this.autoUpdate != other.autoUpdate) {
            return false;
        }
        if (this.autoUpdateSeconds != other.autoUpdateSeconds) {
            return false;
        }
        if (this.enabled != other.enabled) {
            return false;
        }
        return true;
    }

    
    
    
}

