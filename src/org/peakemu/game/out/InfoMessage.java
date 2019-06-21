/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out;

import java.util.ArrayList;
import java.util.Collection;
import org.peakemu.common.util.StringUtil;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class InfoMessage {
    public enum Type{
        INFO, ERROR, PVP
    }
    
    public static class Message{
        final private int id;
        final private Object[] args;

        public Message(int id, Object[] args) {
            this.id = id;
            this.args = args;
        }
    }
    
    final private Type type;
    final private Collection<Message> messages = new ArrayList<>();

    public InfoMessage(Type type) {
        this.type = type;
    }
    
    public InfoMessage addMessage(int id, Object... args){
        messages.add(new Message(id, args));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(8 * messages.size());
        
        sb.append("Im").append(type.ordinal());
        
        boolean b = false;
        for(Message msg : messages){
            if(b)
                sb.append('|');
            else
                b = true;
            
            sb.append(msg.id);
            
            if(msg.args.length > 0)
                sb.append(';').append(StringUtil.join(msg.args, "~"));
        }
        
        return sb.toString();
    }
    
    static public InfoMessage friendConnection(Player friend){
        String param = friend.getAccount().get_pseudo() + " (<b><a href='asfunction:onHref,ShowPlayerPopupMenu," + friend.getName() + "'>" + friend.getName() + "</a></b>)";
        return new InfoMessage(Type.INFO).addMessage(143, param);
    }
}
