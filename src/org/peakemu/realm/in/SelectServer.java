/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.realm.in;

import org.peakemu.game.GameServerConfig;
import org.peakemu.network.InputPacket;
import org.peakemu.realm.RealmClient;
import org.peakemu.realm.RealmServerConfig;
import org.peakemu.realm.out.SelectServerCrypt;
import org.peakemu.realm.out.SelectServerPlain;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SelectServer implements InputPacket<RealmClient>{
    final private RealmServerConfig realmConfig;
    final private GameServerConfig gameConfig;
    final private SessionHandler sessionHandler;

    public SelectServer(RealmServerConfig realmConfig, GameServerConfig gameConfig, SessionHandler sessionHandler) {
        this.realmConfig = realmConfig;
        this.gameConfig = gameConfig;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void parse(String args, RealmClient client) { //args contains server id
        if(client.getAccount() == null)
            return;
        
        String key = sessionHandler.addPendingAccount(client.getAccount());
        
        if(realmConfig.getCryptIp()){
            SelectServerCrypt out = new SelectServerCrypt();
            out.setIp(gameConfig.getIp());
            out.setPort(gameConfig.getPort());
            out.setKey(key);
            client.send(out);
        }else{
            SelectServerPlain out = new SelectServerPlain();
            out.setIp(gameConfig.getIp());
            out.setPort(gameConfig.getPort());
            out.setKey(key);
            client.send(out);
        }
        
        sessionHandler.removeSession(client);
    }

    @Override
    public String header() {
        return "AX";
    }
    
}
