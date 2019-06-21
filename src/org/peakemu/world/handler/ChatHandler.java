/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.EnumMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.game.out.basic.ChatMessageSent;
import org.peakemu.objects.player.Player;
import org.peakemu.world.chat.AdminChat;
import org.peakemu.world.chat.AlignmentChannel;
import org.peakemu.world.chat.ChatChannel;
import org.peakemu.world.chat.DefaultChannel;
import org.peakemu.world.chat.FightTeamChannel;
import org.peakemu.world.chat.GroupChannel;
import org.peakemu.world.chat.GuildChannel;
import org.peakemu.world.chat.RecruitmentChannel;
import org.peakemu.world.chat.TradeChannel;
import org.peakemu.world.config.ChatConfig;
import org.peakemu.world.enums.ChatChannelType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ChatHandler {

    final private Peak peak;
    final private ChatConfig config;
    final private Map<ChatChannelType, ChatChannel> channels = new EnumMap<>(ChatChannelType.class);

    public ChatHandler(Peak peak, ChatConfig config) {
        this.peak = peak;
        this.config = config;
    }

    private void load() {
        if (!channels.isEmpty()) {
            return;
        }

        addChannel(new DefaultChannel(peak.getCommandHandler(), config));
        addChannel(new GuildChannel());
        addChannel(new FightTeamChannel());
        addChannel(new TradeChannel(peak.getWorld().getSessionHandler(), config));
        addChannel(new RecruitmentChannel(peak.getWorld().getSessionHandler(), config));
        addChannel(new AdminChat(peak.getWorld().getSessionHandler()));
        addChannel(new AlignmentChannel(peak.getWorld().getSessionHandler(), config));
        addChannel(new GroupChannel());
    }

    public void addChannel(ChatChannel channel) {
        channels.put(channel.geType(), channel);
    }

    static public String prepareMessage(String message) {
        return message.replace(">", "&#gt;")
                .replace("<", "&lt;")
                .replace("\n", "<br></br>");
    }

    public void sendMessage(ChatChannelType type, Player send, String message) {
        load();

        ChatChannel channel = channels.get(type);

        if (channel == null) {
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "No channel found for %s", type);
            return;
        }

        channel.onMessage(send, message);
    }

    public void sendPrivateMessage(Player sender, Player target, String message) {

        if (sender != null && message != null) 
            sender.send(new ChatMessageSent(ChatChannelType.PRIVATE_TO, target, message));
        

        if (target != null && message != null)
            target.send(new ChatMessageSent(ChatChannelType.PRIVATE_FROM, sender, message));
        
        // TODO target not connected 

    }
}
