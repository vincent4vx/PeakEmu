/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.chat;

import org.peakemu.command.ChatCommandPerformer;
import org.peakemu.command.CommandHandler;
import org.peakemu.game.out.basic.ChatMessageSent;
import org.peakemu.objects.player.Player;
import org.peakemu.world.config.ChatConfig;
import org.peakemu.world.enums.ChatChannelType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DefaultChannel implements ChatChannel{
    final private CommandHandler commandHandler;
    final private ChatConfig config;

    public DefaultChannel(CommandHandler commandHandler, ChatConfig config) {
        this.commandHandler = commandHandler;
        this.config = config;
    }

    @Override
    public ChatChannelType geType() {
        return ChatChannelType.DEFAULT;
    }

    @Override
    public void onMessage(Player sender, String message) {
        if(message.startsWith(config.getCommandStart())){
            String command = message.substring(config.getCommandStart().length());
            commandHandler.perform(new ChatCommandPerformer(sender), command);
            return;
        }
        
        if(sender.getFight() == null){ //send to map
            sender.getMap().sendToMap(new ChatMessageSent(ChatChannelType.DEFAULT, sender, message));
            return;
        }
        
        //send to team fight
        if(sender.getFighter() != null){
            sender.getFight().sendToFight(new ChatMessageSent(ChatChannelType.DEFAULT, sender, message));
            return;
        }
        
        //TODO: spectator
        //sender.getFight().sendToFight(new ChatMessageSent(ChatChannelType.DEFAULT, sender, message));
    }
    
}
