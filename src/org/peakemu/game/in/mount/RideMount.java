/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.mount;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.mount.MountEquipFailed;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RideMount implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        if(client.getPlayer().getMount() == null
            || client.getPlayer().getLevel() < 60
            || !client.getPlayer().getMount().isMountable()
            || !client.getPlayer().getPlayerData().canHaveMount()){
            client.send(new MountEquipFailed(MountEquipFailed.MOUNT_ERROR_RIDE));
            return;
        }
        
        client.getPlayer().toggleOnMount();
    }

    @Override
    public String header() {
        return "Rr";
    }
    
}
