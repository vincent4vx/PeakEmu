/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.common.Formulas;
import org.peakemu.common.SocketManager;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.handler.PlayerHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class StalkingReward implements ActionPerformer {
    final private PlayerHandler playerHandler;

    public StalkingReward(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    @Override
    public int actionId() {
        return 52;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        if (caster.get_traque() != null && caster.get_traque().get_time() == -2) {
            int xp = Formulas.getTraqueXP(caster.getLevel());
            playerHandler.addXp(caster, xp);
            caster.set_traque(null);//On supprime la traque
            SocketManager.GAME_SEND_MESSAGE(caster, "Vous venez de recevoir " + xp + " points d'experiences.", "000000");
        } else {
            SocketManager.GAME_SEND_MESSAGE(caster, "Thomas Sacre : Reviens me voir quand tu aura abatu un ennemi.", "000000");
        }
        
        return true;
    }

}
