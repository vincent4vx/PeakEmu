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
public class MarriedNo implements GameAction{
    final static public int ACTION_ID = 619;

    @Override
    public void start(GameActionArg arg) {
//        Player player = arg.getClient().getPlayer();
//        player.setisOK(0);
//        SocketManager.GAME_SEND_cMK_PACKET_TO_MAP(player.getMap(), "", player.getSpriteId(), player.get_name(), "Non");
//        World.Wedding(World.getMarried(0), World.getMarried(1), 0);
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
