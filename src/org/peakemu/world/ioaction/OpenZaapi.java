/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.ioaction;

import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.game.out.waypoint.CantUseZaapi;
import org.peakemu.game.out.waypoint.ZaapiList;
import org.peakemu.objects.player.Player;
import org.peakemu.world.JobSkill;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.WaypointHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class OpenZaapi extends BasicIOAction{
    final static public int SKILL_ID = 157;
    
    final private WaypointHandler waypointHandler;

    public OpenZaapi(WaypointHandler waypointHandler) {
        this.waypointHandler = waypointHandler;
    }

    @Override
    public void perform(JobSkill skill, MapCell cell, Player player, GameActionArg arg) {
        WaypointHandler.Waypoint waypoint = new WaypointHandler.Waypoint(cell.getMap(), cell);
        
        if(!waypointHandler.canUseZaapi(player, waypoint)){
            player.send(new CantUseZaapi());
            return;
        }
        
        player.setCurrentWaypoint(waypoint);
        player.send(new ZaapiList(waypointHandler.getZaapiList(player, waypoint)));
    }
    
}
