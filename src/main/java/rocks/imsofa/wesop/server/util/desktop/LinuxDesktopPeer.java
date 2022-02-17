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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author lendle
 */
public class LinuxDesktopPeer extends DesktopPeer {

    static {
        final File tempDirectory = new File("/opt/wesop/temp");
        if (!tempDirectory.exists()) {
            tempDirectory.mkdir();
        }
        Thread tempFileDeleteThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        File [] files=tempDirectory.listFiles();
                        for(File file : files){
                            FileUtils.deleteQuietly(file);
                        }
                        Thread.sleep(24*60 * 60 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        tempFileDeleteThread.setDaemon(true);
        tempFileDeleteThread.start();
    }

    @Override
    public List<String> getCommandlineForOpen(File file) {
        try {
            String filename = file.getName().toLowerCase();
            if (filename.endsWith(".bmp")) {
                return this.openBmp(file);
            } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                return this.openJpg(file);
            } else if (filename.endsWith(".mp3")) {
                return this.openMp3(file);
            } else if (filename.endsWith(".mp4")) {
                return this.openMp4(file);
            } else if (filename.endsWith(".pdf")) {
                return this.openPdf(file);
            } else if (filename.endsWith(".doc") || filename.endsWith(".docx") || filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".ppt") || filename.endsWith(".pptx")) {
                return this.openOffice(file);
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
//        return Arrays.asList("cvlc","--loop", "--fullscreen", file.getAbsolutePath());
        try {
            //        return Arrays.asList("vlc", "--fullscreen",file.getAbsolutePath());
            //        return Arrays.asList("mplayer", "-fs", "-loop", "0", file.getAbsolutePath());
            //        return Arrays.asList("mpv", "--loop-file=inf", "--fullscreen=yes", "--no-stop-screensaver", file.getAbsolutePath());

            File newFile = File.createTempFile("tmp", ".mp4", new File("/opt/wesop/temp"));
            FileUtils.copyFile(file, newFile);
            return Arrays.asList("/opt/wesop/launchMp4.sh", newFile.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private List<String> openOffice(File file) {
        String name=file.getName().toLowerCase();
        if(name.endsWith("pptx") || name.endsWith("ppt")){
            return Arrays.asList("libreoffice", "--norestore", "--impress", "--show", file.getAbsolutePath());
        }
        return Arrays.asList("libreoffice", "--norestore", file.getAbsolutePath());
    }

    @Override
    protected DesktopProcess wrap2DesktopProcess(Process process, List<String> commandline) {
        return new LinuxDesktopProcess(process, commandline);
    }
}
