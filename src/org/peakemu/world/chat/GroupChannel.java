/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.chat;

import org.peakemu.game.out.basic.ChatMessageSent;
import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ChatChannelType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GroupChannel implements ChatChannel {

    @Override
    public ChatChannelType geType() {
        return ChatChannelType.GROUP;
    }

    @Override
    public void onMessage(Player sender, String message) {
        
        if(sender.getGroup() != null)
            sender.getGroup().sendToGroup(new ChatMessageSent(geType(), sender, message));
    }

}
