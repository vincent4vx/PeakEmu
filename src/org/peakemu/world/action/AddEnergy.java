/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class AddEnergy implements ActionPerformer {

    @Override
    public int actionId() {
        return 21;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            int Energy = Integer.parseInt(action.getArgs());
            if (Energy < 1) {
                return false;
            }

            int EnergyTotal = caster.getEnergy() + Energy;
            if (EnergyTotal > 10000) {
                EnergyTotal = 10000;
            }

            caster.setEnergy(EnergyTotal);
            SocketManager.GAME_SEND_STATS_PACKET(caster);
            SocketManager.GAME_SEND_Im_PACKET(caster, "07;" + EnergyTotal);
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
        
        return true;
    }

}
