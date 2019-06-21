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
public class Moon implements ActionPerformer {
    final private PlayerHandler playerHandler;

    public Moon(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public int actionId() {
        return -21;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        return false;
        // Casque en bois ou ailes en bois manquant
//        if (!caster.hasEquiped(1019) || !caster.hasEquiped(1021)) {
//            return;
//        }
//        SocketManager.GAME_SEND_GA_PACKET(caster.getAccount().getGameThread(), "", "2", caster.getSpriteId() + "", "1");
//        playerHandler.teleport(caster, (short) 437, 411);
    }

}
