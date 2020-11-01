package com.example.lendle.esopserver.services;

import com.example.lendle.esopserver.DebugUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lendle on 2015/2/8.
 */
public class SequentialTaskThread extends Thread implements TaskExecutionCallback {
    private List<Task> queue=new ArrayList<Task>();
    private boolean running=true;
    private boolean busy=false;

    public SequentialTaskThread(){
        this.setDaemon(true);
    }

    public void run(){
        while(running){
            synchronized(this){
                while(busy || this.queue.isEmpty()){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //execute task
                this.busy=true;
                try {
                    Task task = this.queue.remove(0);
                    task.execute(this);
                }catch(Throwable e){
                    DebugUtils.log(e+":"+e.getMessage());
                }
            }
        }
    }

    public synchronized void addTask(Task task){
        this.queue.add(task);
        this.notifyAll();
    }

    public void shutdown(){
        this.running=false;
        this.interrupt();
    }

    @Override
    public synchronized void onFinished(Task task) {
        this.busy=false;
        this.notifyAll();
    }

    @Override
    public synchronized void onFailed(Task task) {
        this.busy=false;
        this.notifyAll();
    }

    public static interface Task{
        public void execute(TaskExecutionCallback callback);
    }

    public int getQueueLength(){
        return this.queue.size();
    }
}
