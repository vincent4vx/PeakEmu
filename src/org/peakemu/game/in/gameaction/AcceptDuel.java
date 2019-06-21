/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.common.Constants;
import org.peakemu.common.SocketManager;
import org.peakemu.world.fight.Fight;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.fight.FightHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AcceptDuel implements GameAction{
    final static public int ACTION_ID = 901;
    
    final private FightHandler fightHandler;

    public AcceptDuel(FightHandler fightHandler) {
        this.fightHandler = fightHandler;
    }

    @Override
    public void start(GameActionArg arg) {
        Player player = arg.getClient().getPlayer();
        
        int guid = -1;
        try {
            guid = Integer.parseInt(arg.getArg());
        } catch (NumberFormatException e) {
            return;
        }
        
        if (player.get_duelID() != guid || player.get_duelID() == -1) {
            return;
        }
        
        Player target = player.getMap().getPlayer(guid);
        
        SocketManager.GAME_SEND_MAP_START_DUEL_TO_MAP(player.getMap(), player.get_duelID(), player.getSpriteId());
        Fight fight = fightHandler.startChallengeFight(player.getMap(), player, target);
        player.setFight(fight);
        target.setFight(fight);
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
