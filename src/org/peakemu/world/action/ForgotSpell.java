/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.common.SocketManager;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class ForgotSpell implements ActionPerformer {

    @Override
    public int actionId() {
        return 14;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        caster.setisForgetingSpell(true);
        SocketManager.GAME_SEND_FORGETSPELL_INTERFACE('+', caster);
        return true;
    }

}
