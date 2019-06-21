/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.chat;

import org.peakemu.objects.player.Player;
import org.peakemu.world.enums.ChatChannelType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface ChatChannel {
    public ChatChannelType geType();
    public void onMessage(Player sender, String message);
}
