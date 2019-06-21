/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.spell;

import org.peakemu.common.SocketManager;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.spell.SpellUpgradeFailed;
import org.peakemu.game.out.spell.SpellUpgraded;
import org.peakemu.network.InputPacket;
import org.peakemu.world.Spell;
import org.peakemu.world.handler.SpellHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BoostSpell implements InputPacket<GameClient>{
    final private SpellHandler spellHandler;
    final private PlayerDAO playerDAO;

    public BoostSpell(SpellHandler spellHandler, PlayerDAO playerDAO) {
        this.spellHandler = spellHandler;
        this.playerDAO = playerDAO;
    }

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null)
            return;
        
        Spell spell;
        
        try{
            spell = spellHandler.getSpellById(Integer.parseInt(args));
        }catch(NumberFormatException e){
            return;
        }
        
        if(spell == null){
            client.send(new SpellUpgradeFailed());
            return;
        }
        
        if(client.getPlayer().boostSpell(spell)){
            client.send(new SpellUpgraded(client.getPlayer().getSpellById(spell.getId())));
            playerDAO.save(client.getPlayer());
            SocketManager.GAME_SEND_STATS_PACKET(client.getPlayer());
        }else{
            client.send(new SpellUpgradeFailed());
        }
    }

    @Override
    public String header() {
        return "SB";
    }
    
}
