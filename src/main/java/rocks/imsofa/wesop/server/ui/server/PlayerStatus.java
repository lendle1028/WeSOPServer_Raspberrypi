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
public enum PlayerStatus {
    UNKNOWN,
    IDLE,
    CREATED,
    INITIALIZED,
    DOWNLOADING,
    DOWNLOADED,
    PLAYING,
    TERMINATING,
    TERMINATED,
    FAILED,
    OFFLINE
}
