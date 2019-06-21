/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Alignment implements ActionPerformer {

    @Override
    public int actionId() {
        return 11;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        try {
            byte newAlign = Byte.parseByte(action.getArgs().split(",", 2)[0]);
            boolean replace = Integer.parseInt(action.getArgs().split(",", 2)[1]) == 1;
            //Si le caster n'est pas neutre, et qu'on doit pas remplacer, on passe
            if (caster.getAlignement() != Constants.ALIGNEMENT_NEUTRE && !replace) {
                return false;
            }
            caster.modifAlignement(newAlign);
            return true;
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
            return false;
        }
    }

}
