/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.common.SocketManager;
import org.peakemu.game.GameServer;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class ResetCarac implements ActionPerformer {

    @Override
    public int actionId() {
        return 13;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        return false;
//        try {
////		          caster.getBaseStats().addOneStat(125, -caster.getBaseStats().getEffect(125));
////		          caster.getBaseStats().addOneStat(124, -caster.getBaseStats().getEffect(124));
////		          caster.getBaseStats().addOneStat(118, -caster.getBaseStats().getEffect(118));
////		          caster.getBaseStats().addOneStat(123, -caster.getBaseStats().getEffect(123));
////		          caster.getBaseStats().addOneStat(119, -caster.getBaseStats().getEffect(119));
////		          caster.getBaseStats().addOneStat(126, -caster.getBaseStats().getEffect(126));
//            caster.addCapital((caster.getLevel() - 1) * 5 - caster.getCapitalPoints());
//
//            SocketManager.GAME_SEND_STATS_PACKET(caster);
//        } catch (Exception e) {
//            GameServer.addToLog(e.getMessage());
//        }
    }

}
