/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
public class RecruitmentChannel implements ChatChannel{
    final private SessionHandler sessionHandler;
    final private ChatConfig config;
    
    final private Map<Player, Long> lastMessages = new ConcurrentHashMap<>();

    public RecruitmentChannel(SessionHandler sessionHandler, ChatConfig config) {
        this.sessionHandler = sessionHandler;
        this.config = config;
    }

    @Override
    public ChatChannelType geType() {
        return ChatChannelType.RECRUITMENT;
    }

    @Override
    public void onMessage(Player sender, String message) {
        long lastMessage = lastMessages.containsKey(sender) ? lastMessages.get(sender) : 0;
        
        if(lastMessage + config.getRecruitmentFloodTime() > System.currentTimeMillis()){
            sender.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(115, (config.getRecruitmentFloodTime() - (System.currentTimeMillis() - lastMessage)) / 1000));
            return;
        }
        
        sessionHandler.sendToOnline(new ChatMessageSent(geType(), sender, message));
        lastMessages.put(sender, System.currentTimeMillis());
    }
    
}
