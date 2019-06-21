/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.mount;

import org.peakemu.database.dao.MountDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.mount.MountNameChanged;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountChangeName implements InputPacket<GameClient>{
    final private MountDAO mountDAO;

    public MountChangeName(MountDAO mountDAO) {
        this.mountDAO = mountDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getMount() == null)
            return;
        
        client.getPlayer().getMount().setName(args);
        client.send(new MountNameChanged(args));
        
        mountDAO.save(client.getPlayer().getMount());
    }

    @Override
    public String header() {
        return "Rn";
    }
    
}
