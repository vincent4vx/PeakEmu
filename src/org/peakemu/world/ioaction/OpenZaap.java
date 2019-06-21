/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.ioaction;

import org.peakemu.common.util.StringUtil;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.waypoint.WaypointList;
import org.peakemu.objects.player.Player;
import org.peakemu.world.JobSkill;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.WaypointHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class OpenZaap extends BasicIOAction{
    final static public int SKILL_ID = 114;
    
    final private PlayerDAO playerDAO;
    final private WaypointHandler waypointHandler;

    public OpenZaap(PlayerDAO playerDAO, WaypointHandler waypointHandler) {
        this.playerDAO = playerDAO;
        this.waypointHandler = waypointHandler;
    }

    @Override
    public void perform(JobSkill skill, MapCell cell, Player player, GameActionArg arg) {
        if(player.getFight() != null)
            return;
        
        WaypointHandler.Waypoint waypoint = new WaypointHandler.Waypoint(cell.getMap(), cell);
        
        if(!player.hasZaap(waypoint)){
            player.addZaap(waypoint);
            player.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(24));
            playerDAO.save(player);
        }
        
        player.send(new WaypointList(
            StringUtil.split(player.getSavePos(), ",", 2)[0], 
            waypointHandler.getZaapList(player, waypoint
        )));
        
        player.setCurrentWaypoint(waypoint);
    }
    
}
