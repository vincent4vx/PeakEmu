/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.group;

import org.peakemu.game.GameClient;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.group.GroupInvitation;
import org.peakemu.game.out.group.GroupInvitationError;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.player.Group;
import org.peakemu.objects.player.Player;
import org.peakemu.world.config.WorldConfig;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class InviteGroup implements InputPacket<GameClient>{
    final private SessionHandler sessionHandler;
    final private WorldConfig worldConfig;

    public InviteGroup(SessionHandler sessionHandler, WorldConfig worldConfig) {
        this.sessionHandler = sessionHandler;
        this.worldConfig = worldConfig;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getRequest() != null)
            return;
        
        Player target = sessionHandler.searchPlayer(args);
        
        if(target == null){
            client.send(new GroupInvitationError(GroupInvitationError.CANT_FIND_ACCOUNT_OR_CHARACTER, args));
            return;
        }
        
        if(target.isAway()){
            client.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(209));
            return;
        }
        
        if(target.getGroup() != null){
            client.send(new GroupInvitationError(GroupInvitationError.PARTY_ALREADY_IN_GROUP));
            return;
        }
        
        if(client.getPlayer().getGroup() != null && client.getPlayer().getGroup().getSize() >= worldConfig.getMaxGroupSize()){
            client.send(new GroupInvitationError(GroupInvitationError.PARTY_FULL));
            return;
        }
        
        Group.InvitationRequest request = new Group.InvitationRequest(client.getPlayer(), target);
        client.getPlayer().setRequest(request);
        target.setRequest(request);
        
        Object packet = new GroupInvitation(request);
        client.send(packet);
        target.send(packet);
    }

    @Override
    public String header() {
        return "PI";
    }
    
}
