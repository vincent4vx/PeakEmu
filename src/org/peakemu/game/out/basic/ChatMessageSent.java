/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.basic;

import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ChatChannelType;
import org.peakemu.world.handler.ChatHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ChatMessageSent {
    private ChatChannelType type;
    private Player player;
    private String message;

    public ChatMessageSent(ChatChannelType type, Player player, String message) {
        this.type = type;
        this.player = player;
        this.message = message;
    }

    @Override
    public String toString() {
        return "cMK" + type.getC() + "|" + player.getSpriteId() + "|" + player.getName() + "|" + ChatHandler.prepareMessage(message) + "|";
    }
    
    
}
