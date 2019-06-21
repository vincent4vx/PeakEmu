/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.common.SocketManager;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class CreateGuilde implements ActionPerformer {

    @Override
    public int actionId() {
        return -2;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        if (caster.isAway()) {
            return false;
        }
        if (caster.getGuild() != null || caster.getGuildMember() != null) {
            SocketManager.GAME_SEND_gC_PACKET(caster, "Ea");
            return false;
        }
        if (caster.hasItemTemplate(1575, 1)) {
            SocketManager.GAME_SEND_gn_PACKET(caster);
            caster.removeByTemplateID(1575, -1);
            SocketManager.GAME_SEND_Im_PACKET(caster, "022;" + -1 + "~" + 1575);
        } else {
            SocketManager.GAME_SEND_Im_PACKET(caster, "14");
        }
        return true;
    }

}
