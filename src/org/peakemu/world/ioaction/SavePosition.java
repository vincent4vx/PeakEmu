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

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class SavePosition extends BasicIOAction {
    final static public int SKILL_ID = 44;

    @Override
    public void perform(JobSkill skill, MapCell cell, Player player, GameActionArg arg) {
        String str = cell.getMap().getId() + "," + cell.getID();
        player.setSavePos(str);
        SocketManager.GAME_SEND_Im_PACKET(player, "06");
    }

}
