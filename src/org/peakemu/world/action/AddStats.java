/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.common.SocketManager;
import org.peakemu.game.in.account.BoostStat;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.fight.effect.BoostEffect;
import org.peakemu.world.useitemeffect.BoostStatEffect;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class AddStats implements ActionPerformer {

    @Override
    public int actionId() {
        return 8;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            int statID = Integer.parseInt(action.getArgs().split(",", 2)[0]);
            int number = Integer.parseInt(action.getArgs().split(",", 2)[1]);
            caster.getBaseStats().addOneStat(Effect.valueOf(statID), number);
            SocketManager.GAME_SEND_STATS_PACKET(caster);
            
            if (statID == 123)//chance
            {
                SocketManager.GAME_SEND_Im_PACKET(caster, "011;" + number);
            }
            if (statID == 124)//sagesse
            {
                SocketManager.GAME_SEND_Im_PACKET(caster, "09;" + number);
            }
            if (statID == 125)//Vita
            {
                SocketManager.GAME_SEND_Im_PACKET(caster, "013;" + number);
            }
            if (statID == 126)//Intelligence
            {
                SocketManager.GAME_SEND_Im_PACKET(caster, "014;" + number);
            }
            if (statID == 118)//Force
            {
                SocketManager.GAME_SEND_Im_PACKET(caster, "010;" + number);
            }
            if (statID == 119)//Agilit�
            {
                SocketManager.GAME_SEND_Im_PACKET(caster, "012;" + number);
            }
            
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
