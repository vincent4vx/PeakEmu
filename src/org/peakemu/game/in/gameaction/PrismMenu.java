/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.waypoint.PrismList;
import org.peakemu.objects.Prism;
import org.peakemu.objects.player.Player;
import org.peakemu.world.handler.WaypointHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PrismMenu implements GameAction{
    final static public int ACTION_ID = 512;
    
    final private WaypointHandler waypointHandler;

    public PrismMenu(WaypointHandler waypointHandler) {
        this.waypointHandler = waypointHandler;
    }

    @Override
    public void start(GameActionArg arg) {
        Player player = arg.getClient().getPlayer();
        
        if(player == null)
            return;
        
        if(player.getDisgrace() >= 3){
            arg.getClient().send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(83));
            return;
        }
        
        if(!player.showWings()){
            arg.getClient().send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(144));
            return;
        }
        
        Prism prism = player.getMap().getPrism();
        
        if(prism == null || prism.getAlignement() != player.getAlignement())
            return;
        
        arg.getClient().send(new PrismList(waypointHandler.getPrismList(player, prism)));
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
