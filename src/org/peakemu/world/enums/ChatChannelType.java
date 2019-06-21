/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public enum ChatChannelType {
    PRIVATE_FROM('F'),
    PRIVATE_TO('T'),
    FIGHT_TEAM('#'),
    GUILD('%'),
    GROUP('$'),
    ALIGNMENT('!'),
    RECRUITMENT('?'),
    TRADE(':'),
    MEETIC('^'),
    ADMIN('@'),
    DEFAULT('*'),
    ;
    final private char c;
    
    final static private Map<Character, ChatChannelType> types = new HashMap<>();
    
    static{
        for(ChatChannelType cct : values()){
            types.put(cct.getC(), cct);
        }
    }

    private ChatChannelType(char c) {
        this.c = c;
    }

    public char getC() {
        return c;
    }
    
    static public ChatChannelType valueOf(char c){
        return types.get(c);
    }
}
