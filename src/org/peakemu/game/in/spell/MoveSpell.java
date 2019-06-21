/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.spell;

import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.basic.NoOperation;
import org.peakemu.network.InputPacket;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MoveSpell implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        String[] data = StringUtil.split(args, "|", 2);
        
        if(data.length < 2)
            return;
        
        try{
            char pos = (char)('a' + Integer.parseInt(data[1]));
            
            client.getPlayer().setSpellPosition(
                Integer.parseInt(data[0]), 
                pos
            );
        }catch(Exception e){}
        
        client.send(new NoOperation());
    }

    @Override
    public String header() {
        return "SM";
    }
    
}
