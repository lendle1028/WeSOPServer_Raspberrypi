/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

import java.io.File;

/**
 *
 * @author lendle
 */
public class PlaylistPlayItem extends PlayItem{
    protected File [] files=null;

    public File[] getFiles() {
        return files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }
    
}
