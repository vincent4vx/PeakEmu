/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Teleportation implements ActionPerformer {
    final private PlayerHandler playerHandler;

    public Teleportation(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public int actionId() {
        return 0;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            short newMapID = Short.parseShort(action.getArgs().split(",", 2)[0]);
            int newCellID = Integer.parseInt(action.getArgs().split(",", 2)[1]);

            playerHandler.teleport(caster, newMapID, newCellID);
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
