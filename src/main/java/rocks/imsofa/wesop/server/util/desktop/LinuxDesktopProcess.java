/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.util.desktop;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author lendle
 */
public class LinuxDesktopProcess implements DesktopProcess{
    private Process javaProcess=null;
    private List<String> commandLine=null;
    public LinuxDesktopProcess(Process javaProcess, List<String> commandLine){
        this.javaProcess=javaProcess;
        this.commandLine=commandLine;
    }

    @Override
    public Process getJavaProcess() {
        return this.javaProcess;
    }

    @Override
    public void kill() {
        if(this.commandLine.get(0).startsWith("/opt/wesop/launchMp4.sh")){
            try {
                ProcessBuilder pb=new ProcessBuilder("/opt/wesop/stopMp4.sh");
                pb.start();
                this.javaProcess.destroy();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else{
            this.javaProcess.destroy();
        }
    }
    
}
