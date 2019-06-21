/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.account;

import org.peakemu.common.util.StringUtil;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.account.CharacterList;
import org.peakemu.game.out.account.DeleteCharacterError;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DeleteCharacter implements InputPacket<GameClient>{
    final private PlayerHandler playerHandler;

    public DeleteCharacter(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getAccount() == null)
            return;
        
        String[] data = StringUtil.split(args, "|", 2);
        
        Player character = client.getAccount().getCharacterById(Integer.parseInt(data[0]));
        
        if(character == null){
            client.send(new DeleteCharacterError());
            return;
        }
        
        if(character.getLevel() >= 20 && !data[1].equalsIgnoreCase(client.getAccount().get_reponse())){
            client.send(new DeleteCharacterError());
            return;
        }
        
        if(!playerHandler.deleteCharacter(character)){
            client.send(new DeleteCharacterError());
            return;
        }
        
        CharacterList packet = new CharacterList();
        
        for(Player p : client.getAccount().gePlayers()){
            packet.addCharacter(p);
        }
        
        client.send(packet);
    }

    @Override
    public String header() {
        return "AD";
    }
    
}
