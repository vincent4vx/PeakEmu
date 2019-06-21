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
public class AddSpellPoint implements ActionPerformer {

    @Override
    public int actionId() {
        return 20;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            int pts = Integer.parseInt(action.getArgs());
            
            if (pts < 1) {
                return false;
            }
            
            caster.addSpellPoint(pts);
            SocketManager.GAME_SEND_STATS_PACKET(caster);
            SocketManager.GAME_SEND_Im_PACKET(caster, "016;" + pts);
            
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
