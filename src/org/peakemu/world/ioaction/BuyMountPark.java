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
import org.peakemu.game.out.mount.MountParkStartBuy;
import org.peakemu.objects.MountPark;
import org.peakemu.objects.player.Player;
import org.peakemu.world.JobSkill;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class BuyMountPark extends BasicIOAction{
    final static public int SKILL_ID = 176;
    
    final private MountParkDAO mountParkDAO;

    public BuyMountPark(MountParkDAO mountParkDAO) {
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
        
        if(park.isPublic()){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(96));
            return;
        }
        
        if(park.getPrice() <= 0){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(97));
            return;
        }
        
        if(player.getGuild() == null){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(135));
            return;
        }
        
        if(player.getGuildMember().getRank() != 1){
            player.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(98));
            return;
        }
        
        player.send(new MountParkStartBuy(park));
    }
    
}
