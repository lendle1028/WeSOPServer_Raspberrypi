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

/**
 *
 * @author lendle
 */
public abstract class DesktopPeer {
    private static DesktopPeer instance=null;
    public abstract List<String> getCommandlineForOpen(File file);
    public Process open(File file) throws IOException{
        List<String> commandline=new ArrayList<>(this.getCommandlineForOpen(file));
        if(commandline==null){
            throw new IOException("cannot open file");
        }
        ProcessBuilder pb=new ProcessBuilder(commandline);
        return pb.start();
    }
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
