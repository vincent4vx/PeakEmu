/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.ioaction;

import org.peakemu.common.SocketManager;
import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.objects.player.Player;
import org.peakemu.world.JobSkill;
import org.peakemu.world.MapCell;
import org.peakemu.world.config.WorldConfig;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GoIncarnam extends BasicIOAction {
    final static public int SKILL_ID = 183;
    
    final private WorldConfig config;    
    final private PlayerHandler playerHandler;

    public GoIncarnam(WorldConfig config, PlayerHandler playerHandler) {
        this.config = config;
        this.playerHandler = playerHandler;
    }

    @Override
    public void perform(JobSkill skill, MapCell cell, Player player, GameActionArg arg) {
        if (player.getLevel() > config.getMaxIncarnamLevel()) {
            SocketManager.GAME_SEND_Im_PACKET(player, "1127");
            return;
        }
        
        short mapID = player.getRace().getStartMap();
        int cellID = player.getRace().getStartCell();
        playerHandler.teleport(player, mapID, cellID);
        player.setSavePos(mapID + "," + cellID);
    }

}
