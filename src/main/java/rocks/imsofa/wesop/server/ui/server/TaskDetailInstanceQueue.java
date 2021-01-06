/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lendle
 */
public class TaskDetailInstanceQueue {

    private List<TaskDetailInstance> runningQueue = null;
    private List<TaskDetailInstance> terminatingQueue = null;
    private List<TaskDetailInstance> failedQueue = null;
    private List<TaskDetailInstance> purgingQueue=null;
    private List<TaskDetail> dispatchingQueue=null;

    public List<TaskDetailInstance> getRunningQueue() {
        return runningQueue;
    }

    public void setRunningQueue(List<TaskDetailInstance> runningQueue) {
        synchronized (Global.lock) {
            this.runningQueue = runningQueue;
        }
    }

    public List<TaskDetailInstance> getTerminatingQueue() {
        return terminatingQueue;
    }

    public void setTerminatingQueue(List<TaskDetailInstance> terminatingQueue) {
        synchronized (Global.lock) {
            this.terminatingQueue = terminatingQueue;
        }
    }

    public List<TaskDetailInstance> getFailedQueue() {
        return failedQueue;
    }

    public void setFailedQueue(List<TaskDetailInstance> failedQueue) {
        synchronized (Global.lock) {
            this.failedQueue = failedQueue;
        }
    }

    public List<TaskDetailInstance> getPurgingQueue() {
        return purgingQueue;
    }

    public void setPurgingQueue(List<TaskDetailInstance> purgingQueue) {
        synchronized (Global.lock) {
            this.purgingQueue = purgingQueue;
        }
    }

    public List<TaskDetail> getDispatchingQueue() {
        return dispatchingQueue;
    }

    public void setDispatchingQueue(List<TaskDetail> dispatchingQueue) {
        synchronized (Global.lock) {
            this.dispatchingQueue = dispatchingQueue;
        }
    }
    
    
    

    public TaskDetailInstanceQueue() {
        runningQueue = new ArrayList<>();
        terminatingQueue = new ArrayList<>();
        this.failedQueue = new ArrayList<>();
        this.purgingQueue=new ArrayList<>();
        this.dispatchingQueue=new ArrayList<>();
    }
    
    public void removeFailedInstances(String playerId){
        synchronized (Global.lock) {
            List<TaskDetailInstance> temp=new ArrayList<>(this.failedQueue);
            this.failedQueue.clear();
            for(TaskDetailInstance instance : temp){
                if(instance.getPlayerId().equals(playerId)==false){
                    this.failedQueue.add(instance);
                }
            }
        }
    }
    /**
     * purge an existing TaskDetailInstance
     * @param id 
     */
    public void purgeTaskDetailInstances(int id){
        synchronized (Global.lock) {
            for (TaskDetailInstance instance : this.runningQueue) {
                if (instance.getId() == id) {
                    this.purgingQueue.add(instance);
                    return;
                }
            }

            for (TaskDetailInstance instance : this.terminatingQueue) {
                if (instance.getId() == id) {
                    this.purgingQueue.add(instance);
                    return;
                }
            }
        }
    }
    
    public void purgeTaskDetailInstances(TaskDetail taskDetail){
        synchronized (Global.lock) {
            for (TaskDetailInstance instance : this.runningQueue) {
                if (instance.getTaskDetail().equals(taskDetail)) {
                    this.purgingQueue.add(instance);
                    return;
                }
            }

            for (TaskDetailInstance instance : this.terminatingQueue) {
                if (instance.getTaskDetail().equals(taskDetail)) {
                    this.purgingQueue.add(instance);
                    return;
                }
            }
        }
    }

    public TaskDetailInstance getTaskDetailInstance(int id) {
        synchronized (Global.lock) {
            for (TaskDetailInstance instance : this.runningQueue) {
                if (instance.getId() == id) {
                    return instance;
                }
            }

            for (TaskDetailInstance instance : this.terminatingQueue) {
                if (instance.getId() == id) {
                    return instance;
                }
            }
            
            for (TaskDetailInstance instance : this.failedQueue) {
                if (instance.getId() == id) {
                    return instance;
                }
            }
        }

        return null;
    }
}
