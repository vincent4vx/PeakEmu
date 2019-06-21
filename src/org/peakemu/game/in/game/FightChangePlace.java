/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.game;

import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.common.Logger;
import org.peakemu.game.GameClient;
import org.peakemu.game.out.game.FighterChangePositionError;
import org.peakemu.network.InputPacket;
import org.peakemu.world.MapCell;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.fighter.Fighter;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightChangePlace implements InputPacket<GameClient>{

    @Override
    public void parse(String args, GameClient client) {
        if(client.getPlayer() == null || client.getPlayer().getFighter() == null)
            return;
        
        int cellId;
        
        try{
            cellId = Integer.parseInt(args);
        }catch(NumberFormatException e){
            return;
        }
        
        Fight fight = client.getPlayer().getFight();
        Fighter fighter = client.getPlayer().getFighter();
        
        MapCell cell = fighter.getFight().get_map().getCell(cellId);
        
        if(cell == null){
            return;
        }
        
        if(fighter.isReady() || fight.getState() != Constants.FIGHT_STATE_PLACE){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid state");
            client.send(new FighterChangePositionError());
            return;
        }
        
        if(!cell.isWalkable(true)){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Non walkable cell");
            client.send(new FighterChangePositionError());
            return;
        }
            
        if(fight.isOccuped(cellId)){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Occuped cell");
            client.send(new FighterChangePositionError());
            return;
        }
        
        if(!fighter.getTeam().getStartCells().contains(cell)){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid team cell");
            client.send(new FighterChangePositionError());
            return;
        }
        
        fight.changePlace(fighter, cell);
    }

    @Override
    public String header() {
        return "Gp";
    }
    
}
