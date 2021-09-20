/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.util.desktop;

/**
 *
 * @author lendle
 */
public class WindowsDesktopProcess implements DesktopProcess{
    private Process javaProcess=null;
    
    public WindowsDesktopProcess(Process javaProcess){
        this.javaProcess=javaProcess;
    }
    
    @Override
    public Process getJavaProcess() {
        return this.javaProcess;
    }

    @Override
    public void kill() {
        this.javaProcess.destroy();
    }
    
}
