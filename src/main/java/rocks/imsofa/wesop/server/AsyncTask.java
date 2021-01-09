/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server;

/**
 *
 * @author lendle
 */
public abstract class AsyncTask<ARG, RET> {
    protected void onPreExecute(){
    }
    
    protected void onPostExecute(RET ret) {
    
    }
    
    protected abstract RET doInBackground(ARG... params);
    
    /**
     * TODO: (done)implement a custom AsyncTask
     */
    public void execute(ARG... params){
        GlobalContext.executorService.execute(new Runnable() {
            @Override
            public void run() {
                onPreExecute();
                RET ret=doInBackground(params);
                onPostExecute(ret);
            }
        });
//        Thread t=new Thread(){
//            public void run(){
//                onPreExecute();
//                RET ret=doInBackground(params);
//                onPostExecute(ret);
//            }
//        };
//        t.setDaemon(true);
//        t.start();
    }
}
