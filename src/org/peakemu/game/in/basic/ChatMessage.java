/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.basic;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.basic.ChatMessageError;
import org.peakemu.network.InputPacket;
import org.peakemu.world.enums.ChatChannelType;
import org.peakemu.world.handler.ChatHandler;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ChatMessage implements InputPacket<GameClient>{
    final private ChatHandler chatHandler;
    final private SessionHandler session;

    public ChatMessage(ChatHandler chatHandler, SessionHandler session) {
        this.chatHandler = chatHandler;
        this.session = session;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || !client.getPlayer().getRestrictions().canChat())
            return;
        
        String[] data = StringUtil.split(args, "|", 2);
        
        if(data.length != 2){
            client.send(new ChatMessageError(ChatMessageError.SYNTAX_ERROR, ""));
            return;
        }
        
        String target = data[0];
        String message = data[1].substring(0, data[1].length() - 1);
        
        if(target.length() == 1){ //channel
            ChatChannelType type = ChatChannelType.valueOf(target.charAt(0));
            
            if(type == null){
                Peak.worldLog.addToLog(Logger.Level.DEBUG, "Channel not found : %s", target);
                return;
            }
            
            chatHandler.sendMessage(type, client.getPlayer(), message);
        }else{
            
            chatHandler.sendPrivateMessage(client.getPlayer(), session.searchPlayer(target), message);
        }
            
    }

    @Override
    public String header() {
        return "BM";
    }
    
}
