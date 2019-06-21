/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.peakemu.common.SocketManager;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.basic.ChatMessageSent;
import org.peakemu.objects.player.Player;
import org.peakemu.world.config.ChatConfig;
import org.peakemu.world.enums.ChatChannelType;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AlignmentChannel implements ChatChannel {

    final private SessionHandler sessionHandler;
    final private ChatConfig config;

    final private Map<Player, Long> lastMessages = new ConcurrentHashMap<>();

    public AlignmentChannel(SessionHandler sessionHandler, ChatConfig config) {
        this.sessionHandler = sessionHandler;
        this.config = config;
    }

    @Override
    public ChatChannelType geType() {
        return ChatChannelType.ALIGNMENT;
    }

    @Override
    public void onMessage(Player sender, String message) {
        long lastMessage = lastMessages.containsKey(sender) ? lastMessages.get(sender) : 0;

        if (sender.getAlignement() < 1 || sender.getAlignement() > 3) {
            return;
        }
        if (sender.getDeshonor() >= 1) {
            SocketManager.GAME_SEND_Im_PACKET(sender, "183");
            return;
        }

        if (lastMessage + config.getAlignmentFloodTime() > System.currentTimeMillis()) {
            sender.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(115, (config.getAlignmentFloodTime() - (System.currentTimeMillis() - lastMessage)) / 1000));
            return;
        }

        sessionHandler.sendToOnline(new ChatMessageSent(geType(), sender, message));
        lastMessages.put(sender, System.currentTimeMillis());
    }

}
