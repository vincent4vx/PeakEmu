/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.common.SocketManager;
import org.peakemu.objects.player.Player;
import org.peakemu.world.World;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DeclineDuel implements GameAction{
    final static public int ACTION_ID = 902;

    @Override
    public void start(GameActionArg arg) {
        Player player = arg.getClient().getPlayer();
        try {
            if (player.get_duelID() == -1) {
                return;
            }
            
            Player target = player.getMap().getPlayer(player.get_duelID());
            
            SocketManager.GAME_SEND_CANCEL_DUEL_TO_MAP(player.getMap(), player.get_duelID(), player.getSpriteId());
            target.setAway(false);
            target.set_duelID(-1);
            player.setAway(false);
            player.set_duelID(-1);
        } catch (NumberFormatException e) {
            return;
        };
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
