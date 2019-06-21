/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.realm.in;

import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.network.InputPacket;
import org.peakemu.realm.RealmClient;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ServerList implements InputPacket<RealmClient>{
    final private PlayerDAO playerDAO;

    public ServerList(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public void parse(String args, RealmClient client) {
        if(client.getAccount() == null)
            return;
        
        int count = playerDAO.getCharacterCountByAccount(client.getAccount().get_GUID());
        org.peakemu.realm.out.ServerList out = new org.peakemu.realm.out.ServerList(31536000000l);
        
        if(count > 0){
            out.addServer(1, count);
        }
        client.send(out);
    }

    @Override
    public String header() {
        return "Ax";
    }
    
}
