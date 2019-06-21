/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.account;

import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.BeginGameCinematic;
import org.peakemu.game.out.account.CharacterCreation;
import org.peakemu.game.out.account.CharacterList;
import org.peakemu.network.InputPacket;
import org.peakemu.objects.player.Player;
import org.peakemu.world.config.CharacterCreationConfig;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CreateCharacter implements InputPacket<GameClient>{
    final private PlayerDAO playerDAO;
    final private PlayerHandler playerHandler;
    final private CharacterCreationConfig config;

    public CreateCharacter(PlayerDAO playerDAO, PlayerHandler playerHandler, CharacterCreationConfig config) {
        this.playerDAO = playerDAO;
        this.playerHandler = playerHandler;
        this.config = config;
    }

    @Override
    public void parse(String args, GameClient client) {
        
        String[] infos = args.split("\\|");
        
        if (playerDAO.nameExists(infos[0])) {
            client.send(new CharacterCreation(false, CharacterCreation.NAME_ALEREADY_EXISTS));
            return;
        }
        
        if(!playerHandler.isValidName(infos[0])){
            client.send(new CharacterCreation(false, CharacterCreation.CREATE_CHARACTER_BAD_NAME));
            return;
        }
        
        if(playerDAO.getCharacterCountByAccount(client.getAccount().get_GUID()) >= config.getMaxCharPerAccount()){
            client.send(new CharacterCreation(false, CharacterCreation.CREATE_CHARACTER_FULL));
            return;
        }
        
        if(playerHandler.createCharacter(infos[0], Integer.parseInt(infos[1]), Byte.parseByte(infos[2]), Integer.parseInt(infos[3]), Integer.parseInt(infos[4]), Integer.parseInt(infos[5]), client.getAccount())){
            client.send(new CharacterCreation(true));
            
            CharacterList cl = new CharacterList();
            
            for(Player p : client.getAccount().gePlayers()){
                cl.addCharacter(p);
            }
            
            client.send(cl);
            client.send(new BeginGameCinematic());
        }else{
            client.send(new CharacterCreation(false));
        }
    }

    @Override
    public String header() {
        return "AA";
    }
    
}
