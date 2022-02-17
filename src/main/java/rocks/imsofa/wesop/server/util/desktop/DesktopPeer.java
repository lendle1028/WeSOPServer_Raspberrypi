/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.util.desktop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rocks.imsofa.wesop.server.DebugUtils;
import rocks.imsofa.wesop.server.GlobalContext;
import rocks.imsofa.wesop.server.ui.server.Global;

/**
 *
 * @author lendle
 */
public abstract class DesktopPeer {
    private static DesktopPeer instance=null;
    public abstract List<String> getCommandlineForOpen(File file);
    public DesktopProcess open(File file) throws IOException{
        List<String> commandline=new ArrayList<>(this.getCommandlineForOpen(file));
        DebugUtils.log(String.join(" ", commandline));
        if(commandline==null){
            throw new IOException("cannot open file");
        }
        ProcessBuilder pb=new ProcessBuilder(commandline);
        File errorLogFile=new File(GlobalContext.getWeSOPServerHome(), "desktopError.log");
        File outputLogFile=new File(GlobalContext.getWeSOPServerHome(), "desktopOutput.log");
//        pb=pb.redirectErrorStream(true);
//        pb=pb.redirectError(file);
//        pb=pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb=pb.redirectError(errorLogFile);
        pb=pb.redirectOutput(outputLogFile);
        
        return this.wrap2DesktopProcess(pb.start(), commandline);
    }
    
    protected abstract DesktopProcess wrap2DesktopProcess(Process process, List<String> commandline);
    
    public static DesktopPeer getInstance(){
        if(instance!=null){
            return instance;
        }else{
            if(System.getProperty("os.name").toLowerCase().startsWith("win")){
                instance=new WindowsDesktopPeer();
            }else{
                instance=new LinuxDesktopPeer();
            }
            return instance;
        }
    }
}
