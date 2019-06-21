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
public class MarriedYes implements GameAction{
    final public static int ACTION_ID = 618;

    @Override
    public void start(GameActionArg arg) {
//        Player player = arg.getClient().getPlayer();
//        player.setisOK(Integer.parseInt(arg.getArg().substring(0, 1)));
//        SocketManager.GAME_SEND_cMK_PACKET_TO_MAP(player.getMap(), "", player.getSpriteId(), player.get_name(), "Oui");
//        if (World.getMarried(0).getisOK() > 0 && World.getMarried(1).getisOK() > 0) {
//            World.Wedding(World.getMarried(0), World.getMarried(1), 1);
//        }
//        if (World.getMarried(0) != null && World.getMarried(1) != null) {
//            World.PriestRequest((World.getMarried(0) == player ? World.getMarried(1) : World.getMarried(0)), (World.getMarried(0) == player ? World.getMarried(1).getMap() : World.getMarried(0).getMap()), player.get_isTalkingWith());
//        }
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
