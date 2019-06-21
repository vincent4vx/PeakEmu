/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.basic;

import org.peakemu.world.handler.ChatHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ServerMessage {
    private String message;
    
    final static public String INFO_CHAT_COLOR = "009900";
    final static public String MSG_CHAT_COLOR = "111111";
    final static public String EMOTE_CHAT_COLOR = "222222";
    final static public String THINK_CHAT_COLOR = "232323";
    final static public String MSGCHUCHOTE_CHAT_COLOR = "0066FF";;
    final static public String GROUP_CHAT_COLOR = "006699";
    final static public String ERROR_CHAT_COLOR = "C10000";
    final static public String GUILD_CHAT_COLOR = "663399";
    final static public String PVP_CHAT_COLOR = "DD7700";
    final static public String RECRUITMENT_CHAT_COLOR = "737373";
    final static public String TRADE_CHAT_COLOR = "663300";
    final static public String MEETIC_CHAT_COLOR = "0000CC";
    final static public String ADMIN_CHAT_COLOR = "FF00FF";

    public ServerMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "cs" + message;
    }
    
    static public ServerMessage coloredMessage(String color, String message){
        return unsafeColoredMessage(color, ChatHandler.prepareMessage(message));
    }
    
    static public ServerMessage unsafeColoredMessage(String color, String message){
        return new ServerMessage("<font color=\"#" + color + "\">" + message + "</font>");
    }
}
