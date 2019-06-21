/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.account;

import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.account.CharacterList;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ListCharacters implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getAccount() == null)
            return;
        
        CharacterList packet = new CharacterList();
        
        for(Player p : client.getAccount().gePlayers()){
            packet.addCharacter(p);
        }
        
        client.send(packet);
    }

    @Override
    public String header() {
        return "AL";
    }
    
}
