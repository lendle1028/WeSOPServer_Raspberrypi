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
public interface DesktopProcess {
    public Process getJavaProcess();
    public void kill();
}
