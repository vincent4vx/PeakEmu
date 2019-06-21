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
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class LeftJail implements ActionPerformer {
    final private PlayerHandler playerHandler;

    public LeftJail(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public int actionId() {
        return 202;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        long KamasInitial = caster.getKamas();
        long taxe = KamasInitial - 10000;
        if (caster.getKamas() < 10000) {
            return false;
        }
        if (caster.getAlignement() == 1) {
            caster.setKamas(taxe);
            playerHandler.teleport(caster, (short) 6158, 282);
        }
        if (caster.getAlignement() == 2) {
            caster.setKamas(taxe);
            playerHandler.teleport(caster, (short) 6167, 240);
        }
        SocketManager.GAME_SEND_STATS_PACKET(caster);
        return true;
    }

}
