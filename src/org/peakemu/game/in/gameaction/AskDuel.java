/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.Peak;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.player.Player;
import org.peakemu.world.World;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class AskDuel implements GameAction{
    final static public int ACTION_ID = 900;

    @Override
    public void start(GameActionArg arg) {
        Player player = arg.getClient().getPlayer();
        
        if (player.getMap().get_placesStr().equalsIgnoreCase("|")) {
            SocketManager.GAME_SEND_DUEL_Y_AWAY(arg.getClient(), player.getSpriteId());
            return;
        }
        try {
            int guid = Integer.parseInt(arg.getArg());
            if (player.isAway() || player.getFight() != null) {
                SocketManager.GAME_SEND_DUEL_Y_AWAY(arg.getClient(), player.getSpriteId());
                return;
            }
            
            Player target = player.getMap().getPlayer(guid);
            
            if (target == null) {
                return;
            }
            
            if (target.isAway() || target.getFight() != null || target.getMap().getId() != player.getMap().getId()) {
                SocketManager.GAME_SEND_DUEL_E_AWAY(arg.getClient(), player.getSpriteId());
                return;
            }
            
            player.set_duelID(guid);
            player.setAway(true);
            target.set_duelID(player.getSpriteId());
            target.setAway(true);
            SocketManager.GAME_SEND_MAP_NEW_DUEL_TO_MAP(player.getMap(), player.getSpriteId(), guid);
        } catch (NumberFormatException e) {
            return;
        }
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
