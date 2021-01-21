/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.ui;

import javafx.beans.value.ObservableStringValue;

/**
 *
 * @author lendle
 */
public class ClientModel {
    private ObservableStringValue id=null;
    private ObservableStringValue ip=null;
    private ObservableStringValue status=null;
    private ObservableStringValue lastSeen=null;

    public ObservableStringValue getId() {
        return id;
    }

    public void setId(ObservableStringValue id) {
        this.id = id;
    }

    public ObservableStringValue getIp() {
        return ip;
    }

    public void setIp(ObservableStringValue ip) {
        this.ip = ip;
    }

    public ObservableStringValue getStatus() {
        return status;
    }

    public void setStatus(ObservableStringValue status) {
        this.status = status;
    }

    public ObservableStringValue getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(ObservableStringValue lastSeen) {
        this.lastSeen = lastSeen;
    }
    
    
}
