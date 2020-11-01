package com.example.lendle.esopserver;

/**
 * Created by lendle on 2014/11/24.
 */
public class ADBUtils {
  /**
     * force the start of the adb server
     * @throws Exception
     */
    public static void startADBServer() throws Exception{
        ProcessBuilder pb=new ProcessBuilder("adb", "devices");
        Process process=pb.start();
        process.waitFor();
    }

    /**
     * simulate tap at x, y
     * @param x
     * @param y
     * @throws Exception
     */
    public static void tap(int x, int y) throws Exception{
        ProcessBuilder pb=new ProcessBuilder("adb", "shell", "input", "tap", ""+x, ""+y);
        Process process=pb.start();
        process.waitFor();
    }
}
