/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.guild;

import org.peakemu.game.GameClient;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.Guild;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JoinGuildRefuse implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getRequest() == null || !(client.getPlayer().getRequest() instanceof Guild.InvitationRequest))
            return;
        
        if(client.getPlayer().equals(client.getPlayer().getRequest().getPerformer()))
            client.getPlayer().getRequest().cancel();
        else
            client.getPlayer().getRequest().refuse();
    }

    @Override
    public String header() {
        return "gJE";
    }
    
}
