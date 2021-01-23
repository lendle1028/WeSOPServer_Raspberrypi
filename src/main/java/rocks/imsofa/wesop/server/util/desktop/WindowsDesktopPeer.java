/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.util.desktop;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author lendle
 */
public class WindowsDesktopPeer extends DesktopPeer{

    @Override
    public List<String> getCommandlineForOpen(File file) {
        String fileName=file.getName().toLowerCase();
        if(fileName.endsWith(".pdf")){
            return Arrays.asList("windows\\SumatraPDF-3.2-64.exe", "-fullscreen", file.getAbsolutePath());
        }else if(fileName.endsWith(".txt")){
            return Arrays.asList("notepad.exe", file.getAbsolutePath());
        }else {
            return Arrays.asList("windows\\MPC-HCPortable\\MPC-HCPortable.exe", file.getAbsolutePath(), "/fullscreen");
        }
    }
    
}
