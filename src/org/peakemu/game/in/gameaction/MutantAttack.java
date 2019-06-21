/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MutantAttack implements GameAction{
    final static public int ACTION_ID = 910;
    
    final private FightHandler fightHandler;

    public MutantAttack(FightHandler fightHandler) {
        this.fightHandler = fightHandler;
    }

    @Override
    public void start(GameActionArg arg) {
        Player mutant = arg.getClient().getPlayer();
        
        if(mutant == null
            || !mutant.isMutant()
            || !mutant.getRestrictions().canAttack()){
            arg.getClient().send(GameActionResponse.noGameAction());
            return;
        }
        
        Player other = mutant.getMap().getPlayer(Integer.parseInt(arg.getArg()));
        
        if(other == null){
            arg.getClient().send(GameActionResponse.noGameAction());
            return;
        }
        
        if(mutant.isAway() || other.isAway()){
            arg.getClient().send(GameActionResponse.noGameAction());
            return;
        }
        
        fightHandler.startMutantFight(arg.getClient().getPlayer().getMap(), mutant, other);
    }

    @Override
    public void end(GameActionArg arg, boolean success, String args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int actionId() {
        return ACTION_ID;
    }
    
}
