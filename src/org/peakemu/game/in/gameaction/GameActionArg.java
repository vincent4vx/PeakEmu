/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import java.util.HashMap;
import java.util.Map;
import org.peakemu.game.GameClient;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GameActionArg {
    final private GameClient client;
    final private int actionId;
    final private String arg;
    
    final private Map<Object, Object> attachement = new HashMap<>();

    public GameActionArg(GameClient client, int actionId, String arg) {
        this.client = client;
        this.actionId = actionId;
        this.arg = arg;
    }

    public GameClient getClient() {
        return client;
    }

    public int getActionId() {
        return actionId;
    }

    public String getArg() {
        return arg;
    }
    
    public void attach(Object key, Object value){
        attachement.put(key, value);
    }
    
    public Object getAttachement(Object key){
        return attachement.get(key);
    }
}
