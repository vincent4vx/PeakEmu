/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.chat;

import org.peakemu.common.util.Matcher;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.basic.ChatMessageSent;
import org.peakemu.network.DofusClient;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ChatChannelType;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AdminChat implements ChatChannel{
    final private SessionHandler sessionHandler;

    public AdminChat(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public ChatChannelType geType() {
        return ChatChannelType.ADMIN;
    }

    @Override
    public void onMessage(Player sender, String message) {
        if(sender.getAccount().get_gmLvl() < 1)
            return;
        
        sessionHandler.sendToAll(
            new ChatMessageSent(geType(), sender, message), 
            new Matcher<DofusClient>() {
                @Override
                public boolean match(DofusClient obj) {
                    if(!(obj instanceof GameClient))
                        return false;
                    
                    return obj.getAccount().get_gmLvl() > 0;
                }
            }
        );
    }
    
}
