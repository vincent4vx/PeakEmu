/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.group;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.player.Group;
import org.peakemu.world.Request;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AcceptGroupInvitation implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        Request request = client.getPlayer().getRequest();
        
        if(request == null || !(request instanceof Group.InvitationRequest))
            return;
        
        if(!request.getTarget().equals(client.getPlayer()))
            return;
        
        request.accept();
    }

    @Override
    public String header() {
        return "PA";
    }
    
}
