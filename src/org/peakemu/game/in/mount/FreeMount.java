/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.mount;

import org.peakemu.common.SocketManager;
import org.peakemu.database.dao.MountDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.mount.MountUnequiped;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Mount;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FreeMount implements InputPacket<GameClient>{
    final private MountDAO mountDAO;

    public FreeMount(MountDAO mountDAO) {
        this.mountDAO = mountDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getMount() == null)
            return;
        
        Mount mount = client.getPlayer().getMount();
        client.getPlayer().setMount(null);
        
        mountDAO.remove(mount);
        
        client.send(new MountUnequiped());
    }

    @Override
    public String header() {
        return "Rf";
    }
    
}
