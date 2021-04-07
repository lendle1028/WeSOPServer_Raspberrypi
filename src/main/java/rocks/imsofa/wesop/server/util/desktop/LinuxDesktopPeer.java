/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.util.desktop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author lendle
 */
public class LinuxDesktopPeer extends DesktopPeer {

    @Override
    public List<String> getCommandlineForOpen(File file) {
        try {
            String filename=file.getName().toLowerCase();
            if(filename.endsWith(".bmp")){
                return this.openBmp(file);
            }
            else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                return this.openJpg(file);
            } 
            else if (filename.endsWith(".mp3")) {
                return this.openMp3(file);
            } 
            else if (filename.endsWith(".mp4")) {
                return this.openMp4(file);
            } 
            else if (filename.endsWith(".pdf")) {
                return this.openPdf(file);
            } else {
                Path path = file.toPath();
                String mimeType = Files.probeContentType(path);
                ProcessBuilder pb = new ProcessBuilder("xdg-mime", "query", "default", mimeType);
                Process process = pb.start();
                InputStream input = process.getInputStream();
                String ret = IOUtils.toString(input, "utf-8");
                String applicationName = ret.substring(0, ret.indexOf("."));
                if ("vlc".equals(applicationName)) {
                    return Arrays.asList(applicationName, "--loop", "--fullscreen", file.getCanonicalPath());
                } else if ("ristretto".equals(applicationName)) {
                    return Arrays.asList(applicationName, "-f", file.getCanonicalPath());
                }
                return Arrays.asList(applicationName, file.getCanonicalPath());
            }
        } catch (IOException ex) {
            Logger.getLogger(LinuxDesktopPeer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private List<String> openPdf(File file) {
        return Arrays.asList("xpdf", file.getAbsolutePath(), "-fullscreen");
    }

    private List<String> openBmp(File file) {
        return Arrays.asList("feh", "-Z", "-F", file.getAbsolutePath());
    }
    
    private List<String> openJpg(File file) {
        return Arrays.asList("feh", "-Z", "-F", file.getAbsolutePath());
    }
    
    private List<String> openMp3(File file) {
        return Arrays.asList("cvlc", "--loop", "--fullscreen", file.getAbsolutePath());
    }

    private List<String> openMp4(File file) {
        return Arrays.asList("vlc", "--loop", "--fullscreen",file.getAbsolutePath());
    }
}
