/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.group;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.player.Group;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class RefuseGroupInvitation implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getRequest() == null || !(client.getPlayer().getRequest() instanceof Group.InvitationRequest))
            return;
        
        client.getPlayer().getRequest().cancel();
    }

    @Override
    public String header() {
        return "PR";
    }
    
}
