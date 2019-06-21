/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.common.SocketManager;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class StalkingTarget implements ActionPerformer {
    final private SessionHandler sessionHandler;

    public StalkingTarget(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public int actionId() {
        return 51;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        String perr = "";

        perr = item.getStatsTemplate().getText(Effect.NAME);
        
        if (perr == null) {
            return false;
        }
        Player cible = sessionHandler.searchPlayer(perr);
        if (cible == null) {
            caster.send(new InfoMessage(InfoMessage.Type.ERROR).addMessage(211));
            return false;
        }
        SocketManager.GAME_SEND_FLAG_PACKET(caster, cible);
        
        return true;
    }

}
