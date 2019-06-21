/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.game;

import org.peakemu.common.Constants;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.game.FightCellShown;
import org.peakemu.network.InputPacket;
import org.peakemu.world.MapCell;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ShowFightCell implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getFight() == null)
            return;
        
        Fight fight = client.getPlayer().getFight();
        
        if(fight.getState() != Constants.FIGHT_STATE_ACTIVE)
            return;
        
        Fighter fighter = client.getPlayer().getFighter();
        
        if(fighter == null)
            return;
        
        MapCell cell;
        
        try{
            cell = fight.get_map().getCell(Integer.parseInt(args));
        }catch(NumberFormatException e){
            return;
        }
        
        if(cell == null){
            return;
        }
        
        fighter.getTeam().sendToTeam(new FightCellShown(fighter, cell));
    }

    @Override
    public String header() {
        return "Gf";
    }
    
}
