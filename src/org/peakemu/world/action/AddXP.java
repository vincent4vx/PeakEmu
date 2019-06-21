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
public class AddXP implements ActionPerformer {
    final private PlayerHandler playerHandler;

    public AddXP(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public int actionId() {
        return 22;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            long XpAdd = Integer.parseInt(action.getArgs());
            if (XpAdd < 1) {
                return false;
            }
            
            playerHandler.addXp(caster, XpAdd);
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
