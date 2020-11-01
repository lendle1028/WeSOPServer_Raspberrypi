package rocks.imsofa.wesop.server.services;


/**
 * Created by lendle on 2015/2/8.
 */
public interface TaskExecutionCallback {
    public void onFinished(SequentialTaskThread.Task task);
    public void onFailed(SequentialTaskThread.Task task);
}
