/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.ioaction;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.database.dao.MountParkDAO;
import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.exchange.MountParkOpened;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.objects.MountPark;
import org.peakemu.world.JobSkill;
import org.peakemu.world.enums.GuildRight;
import org.peakemu.world.exchange.MountParkExchange;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class OpenMountPark extends BasicIOAction{
    final static public int SKILL_ID = 175;
    
    final private MountParkDAO mountParkDAO;

    public OpenMountPark(MountParkDAO mountParkDAO) {
        this.mountParkDAO = mountParkDAO;
    }

    @Override
    public void perform(JobSkill skill, MapCell cell, Player player, GameActionArg arg) {
        if(player.getDeshonor() >= 5){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(83));
            return;
        }
        
        MountPark park = mountParkDAO.getMountParkByMap(cell.getMap(), true);
        
        if(park == null || !park.getCell().equals(cell)){
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid mount park cell IO :  %d on map %d", cell.getID(), cell.getMap().getId());
            return;
        }
        
        if(!park.isPublic()){
            if(park.getGuild() == null || !park.getGuild().equals(player.getGuild())){
                player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(100));
                return;
            }
            
            if(!player.getGuildMember().hasRight(GuildRight.OTHDINDE)){
                player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(101));
                return;
            }
        }
        
        player.setCurExchange(new MountParkExchange(player, park));
        
        player.send(new MountParkOpened(park.getMounts(player)));
    }
    
}
