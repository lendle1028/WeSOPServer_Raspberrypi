/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui.server;

/**
 *
 * @author lendle
 */
public class FilePlayItem extends PlayItem{
    protected String file=null;

    public FilePlayItem() {
    }

    public FilePlayItem(String file) {
        this.file=file;
    }

    
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    
}
