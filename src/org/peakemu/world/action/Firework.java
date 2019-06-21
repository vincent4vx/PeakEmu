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
import org.peakemu.world.World;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Firework implements ActionPerformer {

    @Override
    public int actionId() {
        return 228;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            int AnimationId = Integer.parseInt(action.getArgs());
            org.peakemu.objects.Fireworks animation = World.getFireworks(AnimationId);
            if (caster.getFight() != null) {
                return false;
            }
            caster.changeOrientation(1);
            SocketManager.GAME_SEND_GA_PACKET_TO_MAP(caster.getMap(), "0", 228, caster.getSpriteId() + ";" + cell.getID() + "," + org.peakemu.objects.Fireworks.PrepareToGA(animation), "");
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
