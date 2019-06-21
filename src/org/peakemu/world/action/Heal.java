/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.common.Formulas;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Heal implements ActionPerformer {

    @Override
    public int actionId() {
        return 10;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            int min = Integer.parseInt(action.getArgs().split(",", 2)[0]);
            int max = Integer.parseInt(action.getArgs().split(",", 2)[1]);
            if (max == 0) {
                max = min;
            }
            int val = Formulas.getRandomValue(min, max);
            
            if (target != null) {
                if (target.getLifePoints() + val > target.getMaxLifePoints()) {
                    val = target.getMaxLifePoints() - target.getLifePoints();
                }
                target.setLifePoints(target.getLifePoints() + val);
                SocketManager.GAME_SEND_STATS_PACKET(target);
                SocketManager.GAME_SEND_Im_PACKET(target, "01;" + val);

            } else {
                if (caster.getLifePoints() + val > caster.getMaxLifePoints()) {
                    val = caster.getMaxLifePoints() - caster.getLifePoints();
                }
                caster.setLifePoints(caster.getLifePoints() + val);
                SocketManager.GAME_SEND_STATS_PACKET(caster);
                SocketManager.GAME_SEND_Im_PACKET(caster, "01;" + val);
            }
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
        
        return true;
    }

}
